package com.rk.pace.presentation.screens.run

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.rk.pace.data.BitmapH
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
    private val bitmapH: BitmapH,
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
        app.launch(i) {
            val bitmapURI: Uri? = bitmapH.saveBitmap(bitmap, "")
            if (bitmapURI == null) {
                println("")
            }
            val runState = runState.value
            saveRunUseCase(
                RunWithPath(
                    run = Run(
                        timestamp = runState.timestamp,
                        durationM = runState.durationInM,
                        distanceMeters = runState.distanceInMeters,
                        avgSpeedMps = 0f,
                        maxSpeedMps = 0f,
                        bitmapURI = bitmapURI ?. toString()
                    ),
                    path = runState.segments.flatten() // issue
                )
            )
            trackerManager.stop()
        }
    }

    fun stopRun() {
        setCaptureBitmap(true)
    }
}