package com.rk.pace.presentation.screens.run

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.di.ApplicationScope
import com.rk.pace.di.IoDispatcher
import com.rk.pace.domain.model.ActRunState
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.tracking.TrackerManager
import com.rk.pace.domain.use_case.SaveRunUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunViewModel @Inject constructor(
    private val trackerManager: TrackerManager,
    private val saveRunUseCase: SaveRunUseCase,
    @param:ApplicationScope
    private val appCoroutineScope: CoroutineScope,
    @param:IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    val actRunState = trackerManager.actRunState
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            ActRunState()
        )

    fun pauseOrStartResumeRun() {
        if (actRunState.value.isAct) {
            trackerManager.pause()
        } else {
            trackerManager.startResume()
        }
    }

    fun finishRun() {
        trackerManager.pause()
//        saveRun()
        trackerManager.stop()
    }

    var isMapLoaded by mutableStateOf(false)
        private set

    fun mapLoaded(loaded: Boolean) {
        isMapLoaded = loaded
    }

    var hasLocationPermission by mutableStateOf(false)
        private set

    fun setLocationPermission(granted: Boolean) {
        hasLocationPermission = granted
    }


    private fun saveRun(run: Run, locations: List<RunPathPoint>) {
        appCoroutineScope.launch(ioDispatcher) {
            saveRunUseCase(run)
        }
    }
}