package com.rk.pace.domain.tracking

import kotlinx.coroutines.flow.Flow

interface GpsStatusTracker {

    val isGpsEnabled: Flow<Boolean>

}