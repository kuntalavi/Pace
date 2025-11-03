package com.rk.pace.domain.tracking

import com.rk.pace.domain.model.RunPathPoint

interface LocationTracker {

    fun setCallback(locationCallback: LocationCallback)

    fun removeCallback()

    interface LocationCallback {
        fun onLocationUpdate(results: List<RunPathPoint>)
    }
}