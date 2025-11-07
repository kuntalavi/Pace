package com.rk.pace.presentation.screens.run

import androidx.lifecycle.ViewModel
import com.rk.pace.common.ut.PathUt.encodePath
import com.rk.pace.common.ut.PathUt.toLatL
import com.rk.pace.di.ApplicationScope
import com.rk.pace.di.IoDispatcher
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunState
import com.rk.pace.domain.model.RunWithPath
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
    private val app: CoroutineScope,
    @param:IoDispatcher
    private val i: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(RunScreenState())
    val state: StateFlow<RunScreenState> = _state

    private val _runState: MutableStateFlow<RunState> = trackerManager.runState
    val runState = _runState

    fun setHasLocationPermission() {
        _state.update { it.copy(hasLocationPermission = true) }
    }

    fun onMapLoaded() {
        _state.update { it.copy(isMapLoaded = true) }
    }

    fun startRun() {
        trackerManager.start()
    }

    fun pauseRun() {
        trackerManager.pause()
    }

    fun resumeRun() {
        trackerManager.resume()
    }

    fun stopRun() {
        saveRun(
            RunWithPath(
                run = Run(
                    timestamp = runState.value.timestamp,
                    durationM = runState.value.durationInM,
                    distanceMeters = runState.value.distanceInMeters,
                    avgSpeedMps = 0f,
                    maxSpeedMps = 0f,
                    ePath = encodePath(runState.value.segments.toLatL())
                ),
                path = runState.value.segments.flatten()
            )
        )
        trackerManager.stop()
    }

    private fun saveRun(run: RunWithPath) {
        app.launch(i) {
            saveRunUseCase(run)
        }
    }
}

// activities
// services
// broadcast receivers
// content providers