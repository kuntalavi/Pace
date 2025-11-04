package com.rk.pace.domain.tracking

import com.rk.pace.common.ut.DistanceUt
import com.rk.pace.domain.mapper.PathUt.toLatLng
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.model.RunState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackerManager @Inject constructor(
    private val backgroundTracker: BackgroundTracker,
    private val locationTracker: LocationTracker,
    private val timeTracker: TimeTracker
) {

    private var isAct = false
        set(value) {
            _runState.update { it.copy(isAct = value) }
            field = value
        }

    private var firstStart = true

    private val _runState = MutableStateFlow(RunState())
    val runState = _runState


    private val timeTrackerCback = { time: Long ->
        _runState.update { it.copy(durationInM = time) }
    }

    private val locationCBack = object : LocationTracker.LocationCallback {
        override fun onLocationUpdate(results: List<RunPathPoint>) {
            if (isAct) {
                results.forEach { info ->
                    updatePath(info)
                }
            }
        }
    }

    private fun updatePath(info: RunPathPoint) {
        _runState.update { runState ->
            val path = runState.path + info.toLatLng()
            val runPathPoints = runState.runPathPoints + info
            runState.copy(
                path = path,
                runPathPoints = runPathPoints,
                distanceInMeters = runState.distanceInMeters.run {
                    var distance = this
                    if (path.size > 1) {
                        distance += DistanceUt.getDistanceBetweenRunPathPoints(
                            runPathPoint1 = path[path.size - 1],
                            runPathPoint2 = path[path.size - 2]
                        )
                    }
                    distance
                },
                speedMps = info.speedMps
            )
        }
    }

    private fun startRunState() {
        _runState.update {
            RunState()
        }
    }

    private fun pauseUpdatePath() {
        _runState.update { actRunState ->
            actRunState.copy(
                path = actRunState.path + emptyList()
            )
        }
    }

    fun startResume() {
        if (isAct) return
        if (firstStart) {
            startRunState()
            backgroundTracker.startBackgroundTracking()
            firstStart = false
        }
        isAct = true
        timeTracker.startResumeTimer { timeTrackerCback }
        locationTracker.setCallback(locationCBack)
    }

    fun pause() {
        isAct = false
        locationTracker.removeCallback()
        timeTracker.pauseTimer()
        pauseUpdatePath()
    }

    fun stop() {
        pause()
        backgroundTracker.stopBackgroundTracking()
        timeTracker.stopTimer()
        startRunState()
        firstStart = true
    }
}