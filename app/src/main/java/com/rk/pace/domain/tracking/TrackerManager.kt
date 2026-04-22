package com.rk.pace.domain.tracking

import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.model.RunState
import kotlinx.coroutines.flow.StateFlow

interface TrackerManager {

    val isRunAct: StateFlow<Boolean>
    val runState: StateFlow<RunState>
    val location: StateFlow<RunPathPoint>
    val gpsStrength: StateFlow<GpsStrength>

    fun start()
    fun pause()
    fun resume()
    fun stop()

}