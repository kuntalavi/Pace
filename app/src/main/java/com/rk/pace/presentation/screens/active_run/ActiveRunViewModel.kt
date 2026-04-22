package com.rk.pace.presentation.screens.active_run

import android.content.Context
import android.os.Build
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.auth.domain.use_case.GetCurrentUserIdUseCase
import com.rk.pace.common.extension.hasPostNotificationPermission
import com.rk.pace.common.extension.hasPreciseForegroundLocationPermission
import com.rk.pace.common.ut.MapUt
import com.rk.pace.common.ut.PathUt.toList
import com.rk.pace.di.ApplicationIoCoroutineScope
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunWithPath
import com.rk.pace.domain.tracking.GpsStatusTracker
import com.rk.pace.domain.tracking.TrackerManager
import com.rk.pace.domain.use_case.run.SaveRunUseCase
import com.rk.pace.presentation.ut.PermissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
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
    @param:ApplicationContext private val context: Context,
    @param:ApplicationIoCoroutineScope private val scope: CoroutineScope,
    private val trackerManager: TrackerManager,
    gpsStatusTracker: GpsStatusTracker,
    private val saveRunUseCase: SaveRunUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {

    private val prefs = context.getSharedPreferences("pace_prefs", Context.MODE_PRIVATE)

    private val _screenState: MutableStateFlow<RunScreenState> =
        MutableStateFlow(RunScreenState.Load)
    val screenState = _screenState.asStateFlow()

    private val _state: MutableStateFlow<RunUiState> = MutableStateFlow(RunUiState())
    val state = _state.asStateFlow()

    val gpsEnabled = gpsStatusTracker.isGpsEnabled.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )
    val runState = trackerManager.runState
    val location = trackerManager.location
    val gpsStrength = trackerManager.gpsStrength

    private var notificationPromptHandled = false

    init {
        observeGps()
        observeLocationPermission()
    }

    private fun observeGps() {
        gpsEnabled.onEach { gpsEnabled ->
            if (gpsEnabled == null) return@onEach
            val currentState = _screenState.value
            val run = runState.value

            if (!gpsEnabled && run.isAct && !run.paused) {
                trackerManager.pause()
                _screenState.update { RunScreenState.GpsDisabledMRun }

            } else if (gpsEnabled && currentState == RunScreenState.GpsDisabledMRun) {
                _screenState.update { RunScreenState.Ready }
            }
        }
            .launchIn(viewModelScope)
    }

    private fun observeLocationPermission() {
        viewModelScope.launch {
            while (isActive) {
                val run = runState.value

                if (
                    run.isAct &&
                    !run.paused &&
                    !context.hasPreciseForegroundLocationPermission()
                ) {
                    trackerManager.pause()
                    _screenState.value = RunScreenState.LocationPermissionRequired(
                        state = locationPermissionState(shouldShowRationale = false),
                        mRun = true
                    )
                }

                delay(1000L)
            }
        }
    }

    fun locationPermissionState(shouldShowRationale: Boolean): PermissionState {
        if (context.hasPreciseForegroundLocationPermission()) return PermissionState.Granted
        return when {
            shouldShowRationale -> PermissionState.DeniedOnce
            !wasLocationEverRequested() -> PermissionState.NotRequested
            else -> PermissionState.DeniedPermanently
        }
    }

    fun notificationPermissionState(shouldShowRationale: Boolean): PermissionState {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return PermissionState.Granted
        if (notificationPromptHandled) return PermissionState.Granted
        if (context.hasPostNotificationPermission()) return PermissionState.Granted
        return when {
            shouldShowRationale -> PermissionState.DeniedOnce
            !wasNotificationEverRequested() -> PermissionState.NotRequested
            else -> PermissionState.DeniedPermanently
        }
    }

    fun skipNotification() {
        notificationPromptHandled = true
    }

    fun markLocationRequested() =
        prefs.edit { putBoolean("location_ever_requested", true) }

    fun markNotificationRequested() {
        notificationPromptHandled = true
        prefs.edit { putBoolean("notification_ever_requested", true) }
    }

    private fun wasLocationEverRequested() =
        prefs.getBoolean("location_ever_requested", false)

    private fun wasNotificationEverRequested() =
        prefs.getBoolean("notification_ever_requested", false)

    fun evaluate(
        shouldShowLocationRationale: Boolean
    ) {
        val locationState = locationPermissionState(shouldShowLocationRationale)
        val run = runState.value

        if (locationState != PermissionState.Granted) {
            if (run.isAct && !run.paused) {
                trackerManager.pause()
            }
            _screenState.value = RunScreenState.LocationPermissionRequired(
                state = locationState,
                mRun = run.isAct
            )
            return
        }

        if (_screenState.value == RunScreenState.GpsDisabledMRun && gpsEnabled.value == false) {
            return
        }
        _screenState.value = RunScreenState.Ready
    }

    fun onTitleChange(newTitle: String) {
        if (state.value.saving) return
        _state.update {
            it.copy(
                title = newTitle
            )
        }
    }

    fun startRun() {
        if (
            !context.hasPreciseForegroundLocationPermission() ||
            gpsEnabled.value != true
        ) return

        trackerManager.start()
        _screenState.value = RunScreenState.Ready
    }

    fun pauseRun() {
        trackerManager.pause()
    }

    fun resumeRun() {
        if (
            !context.hasPreciseForegroundLocationPermission() ||
            gpsEnabled.value != true
        ) return

        trackerManager.resume()
        _screenState.value = RunScreenState.Ready
    }

    fun saveRun() {
        if (state.value.saving) return
        _state.update {
            it.copy(
                saving = true
            )
        }
        scope.launch {
            val currentUserId = getCurrentUserIdUseCase() ?: return@launch
            val runState = runState.value
            val encodedPath = MapUt.encodeSegments(runState.segments)
            val path = runState.segments.toList()
            val run = Run(
                userId = currentUserId,
                timestamp = runState.timestamp,
                durationMilliseconds = runState.durationMilliseconds,
                distanceMeters = runState.distanceMeters,
                avgSpeedMps = runState.avgSpeedMps,
                encodedPath = encodedPath,
                title = state.value.title,
                likes = 0,
                likedBy = emptyList()
            )
            saveRunUseCase(
                RunWithPath(
                    run = run,
                    path = path
                )
            )
            stopRun()
            _state.update {
                it.copy(
                    saved = true,
                    saving = false,
                )
            }
        }
    }

    fun stopRun() {
        trackerManager.stop()
    }
}
