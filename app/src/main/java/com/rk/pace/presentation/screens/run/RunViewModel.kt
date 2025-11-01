package com.rk.pace.presentation.screens.run

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunLocation
import com.rk.pace.domain.use_case.SaveRunUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class RunViewModel @Inject constructor(
    private val saveRunUseCase: SaveRunUseCase
) : ViewModel() {

    var hasLocationPermission by mutableStateOf(false)
        private set

    fun setLocationPermission(granted: Boolean) {
        hasLocationPermission = granted
    }

    val isTracking = MutableStateFlow(false)


    private val _pathPoints = MutableStateFlow<List<List<LatLng>>>(emptyList())
    val pathPoints = _pathPoints

    val runTimeInMillis = MutableStateFlow(0L)
    val runDistanceInMeters = mutableIntStateOf(0)


    private suspend fun saveRun(run: Run, locations: List<RunLocation>) {
        saveRunUseCase(run, locations)
    }
}