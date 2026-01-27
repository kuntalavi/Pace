package com.rk.pace.domain.tracking

import com.rk.pace.domain.model.RunPathPoint
import kotlinx.coroutines.flow.Flow

interface LocationTracker {

    val locationFlow: Flow<RunPathPoint>

}