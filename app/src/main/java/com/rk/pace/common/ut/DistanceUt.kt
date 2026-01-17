package com.rk.pace.common.ut

import android.location.Location
import com.rk.pace.domain.model.RunPathPoint

object DistanceUt {

    fun getDistance(
        point1: RunPathPoint?,
        point2: RunPathPoint
    ): Float {
        if (point1 == null) return 0f
        val result = FloatArray(1)
        Location.distanceBetween(
            point1.lat, point1.long,
            point2.lat, point2.long,
            result
        )
        return if (result[0] > 1f) result[0] else 0f
    }

}