package com.rk.pace.domain.tracking

import com.rk.pace.domain.model.RunPathPoint

interface LocationTracker {

    fun setCback(locationCback: LocationCback)

    fun removeCback()

    interface LocationCback {
        fun onLocationUpdate(results: List<RunPathPoint>)
    }
}