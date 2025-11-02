package com.rk.pace.domain.tracking

import com.rk.pace.domain.model.RunLocation

interface LocationTracker {

    fun setCallback(locationCallback: LocationCallback)

    fun removeCallback()

    interface LocationCallback {
        fun onLocationUpdate(results: List<RunLocation>)
    }
}