package com.rk.pace.presentation.screens.active_run

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.rk.pace.auth.domain.repo.AuthRepo
import com.rk.pace.common.ut.MapUt
import com.rk.pace.common.ut.PathUt.toList
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunState
import com.rk.pace.domain.model.RunWithPath
import com.rk.pace.domain.tracking.TrackerManager
import com.rk.pace.domain.use_case.run.SaveRunUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActiveRunViewModel @Inject constructor(
    private val trackerManager: TrackerManager,
    private val saveRunUseCase: SaveRunUseCase,
    private val authRepo: AuthRepo
) : ViewModel() {

    private val _state = MutableStateFlow(RunScreenState())
    val state: StateFlow<RunScreenState> = _state

    private val _runState: MutableStateFlow<RunState> = trackerManager.runState
    val runState = _runState

    private val _saved = MutableStateFlow(false)
    val saved = _saved

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun onMapLoaded() {
        _state.update { it.copy(isMapLoaded = true) }
    }

    private fun setCaptureBitmap(v: Boolean) {
        _state.update { it.copy(captureBitmap = v) }
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

    fun onBitmapReady(bitmap: Bitmap) {
        setCaptureBitmap(false)
        scope.launch {
            val currentUserId = authRepo.currentUserId ?: return@launch
            val runState = runState.value
            val encodedPath = MapUt.encodeSegments(runState.segments)
            val run = Run(
                userId = currentUserId,
                timestamp = runState.timestamp,
                durationMilliseconds = runState.durationMilliseconds,
                distanceMeters = runState.distanceMeters,
                avgSpeedMps = runState.avgSpeedMps,
                encodedPath = encodedPath,
                title = "",
                likes = 0,
                likedBy = emptyList()
            )
            saveRunUseCase(
                RunWithPath(
                    run = run,
                    path = runState.segments.toList() //
                )
            )
            trackerManager.stop()
            _saved.update {
                true
            }
        }
    }

    fun saveRun() {
        setCaptureBitmap(true)
    }

    fun resetSaveState() {
        _saved.update {
            false
        }
    }

    fun stopRun() {
        trackerManager.stop()
    }

}