package com.rk.pace.domain.tracker

import com.rk.pace.domain.model.RunPathPoint
import kotlinx.coroutines.flow.Flow

interface LocationTracker {

    val passiveLocation: Flow<RunPathPoint?>
    val activeTrackLocation: Flow<RunPathPoint>

}