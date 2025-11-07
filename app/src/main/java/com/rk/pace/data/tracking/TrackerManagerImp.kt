package com.rk.pace.data.tracking

import com.rk.pace.common.ut.DistanceUt
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.model.RunState
import com.rk.pace.domain.tracking.LocationTracker
import com.rk.pace.domain.tracking.RunTrackSM
import com.rk.pace.domain.tracking.TimeTracker
import com.rk.pace.domain.tracking.TrackerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackerManagerImp @Inject constructor(
    private val runTrackSM: RunTrackSM,
    private val locationTracker: LocationTracker,
    private val timeTracker: TimeTracker
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

    private var cSegment: MutableList<RunPathPoint> = mutableListOf()

    private val _runState = MutableStateFlow(RunState())
    override val runState = _runState

    private val timeCback = { time: Long ->
        _runState.update { it.copy(durationInM = time) }
    }

    private val lCback = object : LocationTracker.LocationCback {
        override fun onLocationUpdate(results: List<RunPathPoint>) {
            if (isAct) {
                results.forEach { info ->
                    updatePath(info)
                }
            }
        }
    }

    private fun updatePath(info: RunPathPoint) {
        cSegment.add(info)
        _runState.update { runState ->
            val segments = runState.segments.dropLast(1) + listOf(cSegment.toList())
            runState.copy(
                distanceInMeters = runState.distanceInMeters.run {
                    var distance = this
                    if (cSegment.size > 1) {
                        distance += DistanceUt.getDistanceBetweenRunPathPoints(
                            runPathPoint1 = cSegment[cSegment.size - 1],
                            runPathPoint2 = cSegment[cSegment.size - 2]
                        )
                    }
                    distance
                },
                speedMps = info.speedMps,
                segments = segments
            )
        }
    }

    private fun startRunState() {
        _runState.update {
            RunState()
        }
    }

    private fun pauseUpdatePath() {
        if (cSegment.isNotEmpty()) {
            _runState.update { actRunState ->
                actRunState.copy(segments = actRunState.segments + listOf(cSegment.toList()))
            }
            cSegment.clear()
        }
    }

    override fun start() {
        if (isAct) return
        startRunState()
        isAct = true

        runTrackSM.startRunTrackS()

        cSegment = mutableListOf()
        _runState.update { runState ->
            runState.copy()
        }

        timeTracker.startTimer(timeCback)
        locationTracker.setCback(lCback)
    }

    override fun pause() {
        if (paused) return
        paused = true
        timeTracker.pauseTimer()
        locationTracker.removeCback()
        pauseUpdatePath()
    }

    override fun resume() {
        if (!paused) return
        paused = false
        timeTracker.resumeTimer(timeCback)
        locationTracker.setCback(lCback)
    }

    override fun stop() {
        locationTracker.removeCback()
        timeTracker.stopTimer()
        runTrackSM.stopRunTrackS()
        pauseUpdatePath()
        startRunState()
    }
}