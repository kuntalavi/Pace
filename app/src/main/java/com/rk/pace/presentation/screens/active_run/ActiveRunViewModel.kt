package com.rk.pace.presentation.screens.active_run

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.auth.domain.use_case.GetCurrentUserIdUseCase
import com.rk.pace.common.ut.MapUt
import com.rk.pace.common.ut.PathUt.toList
import com.rk.pace.di.ApplicationIoCoroutineScope
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunWithPath
import com.rk.pace.domain.permission.PermissionManager
import com.rk.pace.domain.permission.UserPref
import com.rk.pace.domain.tracking.GpsStatusTracker
import com.rk.pace.domain.tracking.TrackerManager
import com.rk.pace.domain.use_case.run.SaveRunUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActiveRunViewModel @Inject constructor(
    private val permissionManager: PermissionManager,
    private val prefs: UserPref,
    private val trackerManager: TrackerManager,
    gpsStatusTracker: GpsStatusTracker,
    private val saveRunUseCase: SaveRunUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    @param:ApplicationIoCoroutineScope private val scope: CoroutineScope
) : ViewModel() {

    private val _state: MutableStateFlow<ActiveRunUiState> = MutableStateFlow(ActiveRunUiState())
    val state = _state.asStateFlow()

    val runState = trackerManager.runState
    val location = trackerManager.location
    val gpsStrength = trackerManager.gpsStrength

    val gpsEnabled = gpsStatusTracker.isGpsEnabled
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    private var pTrackAction: PTrackAction? = null

    init {
        observeGps()
    }

    private fun observeGps() {
        gpsEnabled.onEach { enabled ->
            val run = runState.value

            if (!enabled && run.isAct && !run.paused) {
                trackerManager.pause()
                _state.update {
                    it.copy(
                        showGpsInterruptedRationale = true
                    )
                }
            } else {
                checkNotReadyWarn()
            }
        }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ActiveRunAction) {
        when (action) {
            is ActiveRunAction.OnStartClick -> prepareToTrack(PTrackAction.START)
            is ActiveRunAction.OnResumeClick -> prepareToTrack(PTrackAction.RESUME)
            is ActiveRunAction.OnPauseClick -> trackerManager.pause()
            is ActiveRunAction.OnStopClick -> trackerManager.stop()
            is ActiveRunAction.OnSaveClick -> saveRun()
            is ActiveRunAction.OnRunTitleChange -> {
                if (state.value.saving) return
                _state.update {
                    it.copy(
                        runTitle = action.title
                    )
                }
            }
            is ActiveRunAction.ClearSaveError -> {
                _state.update {
                    it.copy(
                        saveError = null
                    )
                }
            }

            is ActiveRunAction.DismissAllRationale -> {
                _state.update {
                    it.copy(
                        showLocationPermissionRationale = false,
                        showLocationPermissionSttRationale = false,
                        showNotificationPermissionRationale = false,
                        showNotificationPermissionSttRationale = false,
                        showGpsOffRationale = false,
                        showGpsInterruptedRationale = false
                    )
                }
                pTrackAction = null
            }

            is ActiveRunAction.RequestLocationPermission -> prefs.markLocationPermissionRequested()
            is ActiveRunAction.RequestNotificationPermission -> prefs.markNotificationPermissionRequested()
            is ActiveRunAction.SkipNotificationPermission -> {
                prefs.markNotificationPermissionRequested()
                _state.update {
                    it.copy(
                        showNotificationPermissionRationale = false,
                        showNotificationPermissionSttRationale = false
                    )
                }
                doTrackAction()
            }

            is ActiveRunAction.OnLocationPermissionResult -> handleLocationPermissionResult(
                action.granted,
                action.shouldShowRationale
            )

            is ActiveRunAction.OnNotificationPermissionResult -> handleNotificationPermissionResult(
                action.granted,
                action.shouldShowRationale
            )

            is ActiveRunAction.CheckNotReadyWarn -> checkNotReadyWarn()
        }
    }

    private fun checkNotReadyWarn() {
        val warning = when {
            !permissionManager.hasPreciseLocationPermission() -> NotReadyWarn.LOCATION_PERMISSION_NOT_GRANTED
            !gpsEnabled.value -> NotReadyWarn.GPS_OFF
            else -> null
        }
        _state.update {
            it.copy(
                warning = warning
            )
        }
    }

    private fun prepareToTrack(action: PTrackAction) {
        pTrackAction = action

        if (!permissionManager.hasPreciseLocationPermission()) {
            if (prefs.wasLocationPermissionRequested()) {
                _state.update {
                    it.copy(
                        showLocationPermissionSttRationale = true
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        showLocationPermissionRationale = true
                    )
                }
            }
            return
        }

        if (!gpsEnabled.value) {
            _state.update {
                it.copy(
                    showGpsOffRationale = true
                )
            }
            return
        }

        if (!permissionManager.hasNotificationPermission()) {
            if (prefs.wasNotificationPermissionRequested()) {
                _state.update {
                    it.copy(
                        showNotificationPermissionSttRationale = true
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        showNotificationPermissionRationale = true
                    )
                }
            }
        }

        doTrackAction()
    }

    private fun handleLocationPermissionResult(
        granted: Boolean,
        shouldShowRationale: Boolean
    ) {
        _state.update {
            it.copy(
                showLocationPermissionRationale = false
            )
        }
        if (granted) {
            prepareToTrack(pTrackAction ?: return)
        } else if (!shouldShowRationale) {
            _state.update {
                it.copy(
                    showLocationPermissionSttRationale = true
                )
            }
        }
    }

    private fun handleNotificationPermissionResult(
        granted: Boolean,
        shouldShowRationale: Boolean
    ) {
        _state.update {
            it.copy(
                showNotificationPermissionRationale = false
            )
        }
        if (granted) {
            doTrackAction()
        } else if (!shouldShowRationale) {
            _state.update {
                it.copy(
                    showNotificationPermissionSttRationale = true
                )
            }
        } else {
            doTrackAction()
        }
    }

    private fun doTrackAction() {
        when (pTrackAction) {
            PTrackAction.START -> trackerManager.start()
            PTrackAction.RESUME -> trackerManager.resume()
            null -> {}
        }
        pTrackAction = null
    }

    private fun saveRun() {
        if (state.value.saving) return
        _state.update {
            it.copy(
                saving = true
            )
        }

        viewModelScope.launch {

            val defferedSave = scope.async {
                try {
                    val currentUserId = getCurrentUserIdUseCase() ?: return@async false
                    val run = runState.value
                    val encodedPath = MapUt.encodeSegments(run.segments)
                    val r = Run(
                        userId = currentUserId,
                        timestamp = run.timestamp,
                        durationMilliseconds = run.durationMilliseconds,
                        distanceMeters = run.distanceMeters,
                        avgSpeedMps = run.avgSpeedMps,
                        encodedPath = encodedPath,
                        title = state.value.runTitle,
                        likes = 0,
                        likedBy = emptyList()
                    )
                    val path = run.segments.toList()
                    saveRunUseCase(
                        RunWithPath(
                            run = r,
                            path = path
                        )
                    )
                    true
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            }
            val success = defferedSave.await()

            if (success) {
                trackerManager.stop()
                _state.update {
                    it.copy(
                        isRunSaved = true,
                        saving = false,
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        saving = false,
                        saveError = "Try Again"
                    )
                }
            }

        }
    }

}
