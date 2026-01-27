package com.rk.pace.presentation.screens.active_run

import androidx.lifecycle.ViewModel
import com.rk.pace.auth.domain.use_case.GetCurrentUserIdUseCase
import com.rk.pace.common.ut.MapUt
import com.rk.pace.common.ut.PathUt.toList
import com.rk.pace.di.ApplicationIoCoroutineScope
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunState
import com.rk.pace.domain.model.RunWithPath
import com.rk.pace.domain.tracking.TrackerManager
import com.rk.pace.domain.use_case.run.SaveRunUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActiveRunViewModel @Inject constructor(
    @param:ApplicationIoCoroutineScope private val scope: CoroutineScope,
    private val trackerManager: TrackerManager,
    private val saveRunUseCase: SaveRunUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {
    private val _runState: MutableStateFlow<RunState> = trackerManager.runState
    val runState = _runState.asStateFlow()

    val location = trackerManager.location
    val gpsStrength = trackerManager.gpsStrength

    private val _state: MutableStateFlow<RunUiState> = MutableStateFlow(RunUiState())
    val state = _state.asStateFlow()

    fun onTitleChange(t: String) {
        if (state.value.isSav) return
        _state.update {
            it.copy(
                title = t
            )
        }
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

    fun saveRun() {
        if (state.value.isSav) return
        _state.update {
            it.copy(
                isSav = true
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
                    path = path //
                )
            )
            stopRun()
            _state.update {
                it.copy(
                    isSaved = true,
                    isSav = false,
                )
            }
        }
    }

    fun stopRun() {
        trackerManager.stop()
    }
}