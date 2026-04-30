package com.rk.pace.domain.tracker

import kotlinx.coroutines.flow.Flow

interface GpsStatusTracker {

    val isGpsEnabled: Flow<Boolean>

}