package com.rk.pace.data.tracking

import com.rk.pace.common.ut.DistanceUt.getDistance
import com.rk.pace.di.ApplicationDefaultCoroutineScope
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.model.RunState
import com.rk.pace.domain.tracking.GpsStrength
import com.rk.pace.domain.tracking.LocationTracker
import com.rk.pace.domain.tracking.RunTrackC
import com.rk.pace.domain.tracking.TimeTracker
import com.rk.pace.domain.tracking.TrackerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackerManagerImp @Inject constructor(
    @param:ApplicationDefaultCoroutineScope private val scope: CoroutineScope,
    private val runTrackC: RunTrackC,
    private val locationT: LocationTracker,
    private val timeT: TimeTracker
) : TrackerManager {

    override val location: StateFlow<RunPathPoint> = locationT.locationFlow
        .stateIn(
            scope = scope,
            SharingStarted.WhileSubscribed(5000L),
            RunPathPoint(
                lat = 0.0,
                long = 0.0
            )
        )

    override val gpsStrength: StateFlow<GpsStrength> = locationT.locationFlow
        .map { point ->
            when {
                point.accuracy <= 10f -> GpsStrength.STRONG
                point.accuracy <= 20f -> GpsStrength.MODERATE
                else -> GpsStrength.WEAK
            }
        }
        .stateIn(
            scope = scope,
            SharingStarted.WhileSubscribed(5000),
            GpsStrength.NONE
        )

    private var isAct = false
        set(value) {
            _runState.update { runState ->
                runState.copy(
                    isAct = value
                )
            }
            field = value
        }

    private var paused = false
        set(value) {
            _runState.update { runState ->
                runState.copy(
                    paused = value
                )
            }
            field = value
        }

    private val _runState = MutableStateFlow(RunState())
    override val runState = _runState

    private var job: Job? = null

    private var locationPoint: RunPathPoint? = null
    private var totalDistance = 0f
    private var currentSegment: MutableList<RunPathPoint> = mutableListOf()

    private val timeCallback = { time: Long ->
        _runState.update { runState ->
            runState.copy(
                durationMilliseconds = time
            )
        }
    }

    private fun updatePath(point: RunPathPoint) {
        val newDistance = getDistance(
            locationPoint,
            point
        )
        totalDistance += newDistance

        locationPoint = point
        currentSegment += point

        _runState.update { runState ->
            val segments = runState.segments.dropLast(1) + listOf(currentSegment.toList())

            val durationSeconds = runState.durationMilliseconds / 1000f
            val avgSpeedMps = if (durationSeconds > 0) {
                totalDistance / durationSeconds
            } else 0f

            runState.copy(
                distanceMeters = totalDistance,
                avgSpeedMps = avgSpeedMps,
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

    private fun startJob() {
        if (job?.isActive == true) return

        job = locationT.locationFlow
            .onEach { point ->
                updatePath(point)
            }
            .launchIn(scope)
    }

    private fun stopJob() {
        job?.cancel()
        job = null
        locationPoint = null
    }

    override fun start() {
        if (isAct) return
        currentSegment = mutableListOf()
        locationPoint = null
        totalDistance = 0f
        resetRunState()
        isAct = true

        runTrackC.startRunTrackS()
        timeT.startTimer(timeCallback)
        startJob()
    }

    override fun pause() {
        if (paused) return
        paused = true

        pauseUpdatePath()

        timeT.pauseTimer()
        stopJob()
    }

    override fun resume() {
        if (!paused) return
        paused = false

        timeT.resumeTimer(timeCallback)
        startJob()
    }

    override fun stop() {
        isAct = false
        paused = false
        pauseUpdatePath()

        runTrackC.stopRunTrackS()
        timeT.stopTimer()
        stopJob()
        totalDistance = 0f
        resetRunState()
    }

}