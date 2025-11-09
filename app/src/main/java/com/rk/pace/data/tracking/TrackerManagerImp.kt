package com.rk.pace.data.tracking

import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.model.RunState
import com.rk.pace.domain.tracking.LocationTracker
import com.rk.pace.domain.tracking.RunTrackC
import com.rk.pace.domain.tracking.TimeTracker
import com.rk.pace.domain.tracking.TrackerManager
import kotlinx.coroutines.flow.MutableStateFlow
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

    private var cSegment: MutableList<RunPathPoint> = mutableListOf()

    private val _runState = MutableStateFlow(RunState())
    override val runState = _runState

    private val timeCback = { time: Long ->
        _runState.update { it.copy(durationInM = time) }
    }

    private val locationCback = object : LocationTracker.LocationCback {
        override fun onLocationUpdate(results: List<RunPathPoint>) {
            if (isAct) {
                results.forEach { point ->
                    updatePath(point)
                }
            }
        }
    }

    private fun updatePath(point: RunPathPoint) {
        cSegment += point
        _runState.update { state ->
            val segments = state.segments.dropLast(1) + listOf(cSegment.toList())
            state.copy(
                distanceInMeters = 0f,
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
        if (cSegment.isNotEmpty()) {
            _runState.update { state ->
                state.copy(
                    segments = state.segments + listOf(cSegment.toList())
                )
            }
            cSegment = mutableListOf()
        }
    }

    override fun start() {
        if (isAct) return
        cSegment = mutableListOf()
        resetRunState()
        isAct = true

        runTrackC.startRunTrackS()
        locationT.setCback(locationCback)
        timeT.startTimer(timeCback)
    }

    override fun pause() {
        if (paused) return
        paused = true

        pauseUpdatePath()

        timeT.pauseTimer()
        locationT.removeCback()
    }

    override fun resume() {
        if (!paused) return
        paused = false

        locationT.setCback(locationCback)
        timeT.resumeTimer(timeCback)
    }

    override fun stop() {
        isAct = false
        paused = false
        pauseUpdatePath()

        runTrackC.stopRunTrackS()
        locationT.removeCback()
        timeT.stopTimer()

        resetRunState()
    }
}