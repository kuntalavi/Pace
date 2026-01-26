package com.rk.pace.data.tracking

import com.rk.pace.common.ut.DistanceUt.getDistance
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.model.RunState
import com.rk.pace.domain.tracking.GpsStrength
import com.rk.pace.domain.tracking.LocationTracker
import com.rk.pace.domain.tracking.RunTrackC
import com.rk.pace.domain.tracking.TimeTracker
import com.rk.pace.domain.tracking.TrackerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackerManagerImp @Inject constructor(
    private val runTrackC: RunTrackC,
    private val locationT: LocationTracker,
    private val timeT: TimeTracker
) : TrackerManager {

    private var isAct = false
        set(v) {
            _runState.update { it.copy(isAct = v) }
            field = v
        }

    private var paused = false
        set(v) {
            _runState.update { it.copy(paused = v) }
            field = v
        }

    private var locationPoint: RunPathPoint? = null
    private var totalDistance = 0f

    private var currentSegment: MutableList<RunPathPoint> = mutableListOf()

    private val _runState = MutableStateFlow(RunState())
    override val runState = _runState

    private val timeCallback = { time: Long ->
        _runState.update { it.copy(durationMilliseconds = time) }
    }

    private val _gpsStrength = MutableStateFlow(GpsStrength.NONE)

    override val gpsStrength: StateFlow<GpsStrength> = _gpsStrength

    private val locationCallback = object : LocationTracker.LocationCallback {
        override fun onLocationUpdate(results: List<RunPathPoint>) {
            results.lastOrNull()?.let { point ->
                updateGpsStrength(point.accuracy)
            }
            if (isAct) {
                results.forEach { point ->
                    updatePath(point)
                }
            }
        }
    }

    private fun updateGpsStrength(accuracy: Float) {
        _gpsStrength.update {
            when {
                accuracy <= 10f -> GpsStrength.STRONG
                accuracy <= 20f -> GpsStrength.MODERATE
                else -> GpsStrength.WEAK
            }
        }
    }

    private fun updatePath(point: RunPathPoint) {

        val newDistance = getDistance(locationPoint, point)
        totalDistance += newDistance

        locationPoint = point
        currentSegment += point

        _runState.update { state ->
            val segments = state.segments.dropLast(1) + listOf(currentSegment.toList())

            state.copy(
                distanceMeters = totalDistance,
                avgSpeedMps = totalDistance / (state.durationMilliseconds / 1000L),
                speedMps = point.speedMps,
                segments = segments
            )

        }
    }

    private fun resetRunState() {
        _runState.update {
            RunState()
        }
    }

    private fun pauseUpdatePath() {
        if (currentSegment.isNotEmpty()) {
            _runState.update { state ->
                state.copy(
                    segments = state.segments + listOf(currentSegment.toList())
                )
            }
            currentSegment = mutableListOf()
        }
    }

    override fun start() {
        if (isAct) return
        currentSegment = mutableListOf()
        locationPoint = null
        totalDistance = 0f
        resetRunState()
        isAct = true

        runTrackC.startRunTrackS()
        locationT.setCallback(locationCallback)
        timeT.startTimer(timeCallback)
    }

    override fun pause() {
        if (paused) return
        paused = true

        pauseUpdatePath()

        timeT.pauseTimer()
        locationT.removeCallback()
        locationPoint = null
    }

    override fun resume() {
        if (!paused) return
        paused = false

        locationT.setCallback(locationCallback)
        timeT.resumeTimer(timeCallback)
        locationPoint = null
    }

    override fun stop() {
        isAct = false
        paused = false
        pauseUpdatePath()

        runTrackC.stopRunTrackS()
        locationT.removeCallback()
        timeT.stopTimer()

        locationPoint = null
        totalDistance = 0f
        resetRunState()
        _gpsStrength.update {
            GpsStrength.NONE
        }
    }
}