package com.rk.pace.presentation.screens.run

import androidx.lifecycle.ViewModel
import com.rk.pace.di.ApplicationScope
import com.rk.pace.di.IoDispatcher
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunState
import com.rk.pace.domain.tracking.TrackerManager
import com.rk.pace.domain.use_case.SaveRunUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunViewModel @Inject constructor(
    private val trackerManager: TrackerManager,
    private val saveRunUseCase: SaveRunUseCase,
    @param:ApplicationScope
    private val appScope: CoroutineScope,
    @param:IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(RunScreenState())
    val state: StateFlow<RunScreenState> = _state

    private val _runState: MutableStateFlow<RunState> = trackerManager.runState
    val runState = _runState

    fun onLocationPermissionGranted() {
        _state.update { it.copy(hasLocationPermission = true) }
    }

    fun onMapLoaded() {
        _state.update { it.copy(isMapLoaded = true) }
    }

    fun pauseOrStartResumeRun() {
        if (runState.value.isAct) {
            trackerManager.pause()
        } else {
            trackerManager.startResume()
        }
    }

    fun stopRun() {
        trackerManager.pause()

        saveRun(
            Run(
                startTime = 0L, //
                endTime = 0L, //
                distanceMeters = runState.value.distanceInMeters,
                durationM = runState.value.durationInM,
                avgPace = 0f, //
                maxPace = 0f, //
                path = runState.value.runPathPoints,
            )
        )
        trackerManager.stop()
    }

    private fun saveRun(run: Run) {
        appScope.launch(ioDispatcher) {
            saveRunUseCase(run)
        }
    }
}

// activities
// services
// broadcast receivers
// content providers