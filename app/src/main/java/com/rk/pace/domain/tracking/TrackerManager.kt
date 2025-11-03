package com.rk.pace.domain.tracking

import com.rk.pace.common.ut.DistanceUt
import com.rk.pace.domain.model.ActRunState
import com.rk.pace.domain.model.RunPathPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            _actRunState.update { it.copy(isAct = value) }
            field = value
        }

    private var firstStart = true

    private val _actRunState = MutableStateFlow(ActRunState())
    val actRunState = _actRunState

    private val _durationInM = MutableStateFlow(0L)
    val durationInM = _durationInM.asStateFlow()

    private val timeTrackerCback = { time: Long ->
        _durationInM.update { time }
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
        _actRunState.update { actRunState ->
            val path = actRunState.path + info
            actRunState.copy(
                path = path,
                distanceMeters = actRunState.distanceMeters.run {
                    var distance = this
                    if (path.size > 1) {
                        distance += DistanceUt.getDistanceBetweenRunPathPoints(
                            runPathPoint1 = path[path.size - 1],
                            runPathPoint2 = path[path.size - 2]
                        )
                    }
                    distance
                }
            )
        }
    }

    private fun startRunState() {
        _actRunState.update {
            ActRunState()
        }
        _durationInM.update { 0 }
    }

    private fun pauseUpdatePath() {
        _actRunState.update { actRunState ->
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