package com.rk.pace.data.tracking

import com.rk.pace.common.ut.DistanceUt.getDistance
import com.rk.pace.di.ApplicationDefaultCoroutineScope
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.model.RunState
import com.rk.pace.domain.tracking.GpsStrength
import com.rk.pace.domain.tracking.LocationTracker
import com.rk.pace.domain.tracking.RunTrackServiceController
import com.rk.pace.domain.tracking.TimeTracker
import com.rk.pace.domain.tracking.TrackerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
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
    private val runTrackServiceController: RunTrackServiceController,
    private val locationTracker: LocationTracker,
    private val timeTracker: TimeTracker
) : TrackerManager {

    private val _runState = MutableStateFlow(RunState())
    override val runState = _runState.asStateFlow()

    override val isAct: StateFlow<Boolean> = runState
        .map { it.isAct }
        .distinctUntilChanged()
        .stateIn(
            scope = scope,
            SharingStarted.WhileSubscribed(5000L),
            false
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    override val location: StateFlow<RunPathPoint?> =
        isAct.flatMapLatest { isAct ->
            if (isAct) locationTracker.activeTrackLocation
            else locationTracker.passiveLocation
        }.stateIn(
            scope = scope,
            SharingStarted.WhileSubscribed(5000L),
            null
        )

    override val gpsStrength: StateFlow<GpsStrength> = location
        .map { point ->
            when {
                point == null || point.accuracy == 0f -> GpsStrength.NONE
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

    private var job: Job? = null
    private var point: RunPathPoint? = null
    private var tDistance = 0f
    private var lastSegment: MutableList<RunPathPoint> = mutableListOf()

    private val timeCallback = { time: Long ->
        _runState.update { state ->
            state.copy(
                durationMilliseconds = time
            )
        }
    }

    private fun updatePath(point: RunPathPoint) {
        _runState.update { state ->
            val newDistance = getDistance(
                this@TrackerManagerImp.point,
                point
            )
            tDistance += newDistance

            this@TrackerManagerImp.point = point
            lastSegment.add(
                point
            )

            val isNewSegment = lastSegment.size == 1

            val segments = if (isNewSegment) {
                state.segments + listOf(
                    lastSegment.toList()
                )
            } else {
                state.segments.dropLast(1) + listOf(
                    lastSegment.toList()
                )
            }

            val durationSeconds = state.durationMilliseconds / 1000f
            val avgSpeedMps = if (durationSeconds > 0) {
                tDistance / durationSeconds
            } else 0f

            state.copy(
                distanceMeters = tDistance,
                avgSpeedMps = avgSpeedMps,
                speedMps = point.speedMps,
                segments = segments
            )
        }
    }

    private fun pauseUpdatePath() {
        lastSegment = mutableListOf()
        point = null
    }

    private fun startJob() {
        if (job?.isActive == true) return

        job = locationTracker.activeTrackLocation
            .onEach { point ->
                updatePath(point)
            }
            .launchIn(scope)
    }

    private fun stopJob() {
        job?.cancel()
        job = null
        point = null
    }

    override fun start() {
        if (isAct.value) return
        lastSegment = mutableListOf()
        point = null
        tDistance = 0f
        _runState.update {
            RunState(
                isAct = true
            )
        }

        runTrackServiceController.startRunTrackService()
        timeTracker.startTimer(timeCallback)
        startJob()
    }

    override fun pause() {
        if (runState.value.paused) return
        _runState.update { state ->
            state.copy(
                paused = true
            )
        }

        pauseUpdatePath()

        timeTracker.pauseTimer()
        stopJob()
    }

    override fun resume() {
        if (!runState.value.paused) return
        _runState.update { state ->
            state.copy(
                paused = false
            )
        }

        timeTracker.resumeTimer(timeCallback)
        startJob()
    }

    override fun stop() {
        pauseUpdatePath()

        runTrackServiceController.stopRunTrackService()
        timeTracker.stopTimer()
        stopJob()

        tDistance = 0f
        lastSegment = mutableListOf()
        point = null

        _runState.update {
            RunState()
        }
    }

}