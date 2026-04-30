package com.rk.pace.domain.ut

import com.rk.pace.domain.model.RunPathPoint
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object DistanceUt {

    private const val EARTH_RADIUS_METERS = 6371000f

    /**
     * Haversine Formula
     * Returns Distance Between Two Points In Meters
     */
    fun getDistance(
        point1: RunPathPoint?,
        point2: RunPathPoint
    ): Float {

        if (point1 == null) return 0f

        val lat1 = Math.toRadians(point1.lat)
        val lat2 = Math.toRadians(point2.lat)
        val deltaLat = Math.toRadians(point2.lat - point1.lat)
        val deltaLong = Math.toRadians(point2.long - point1.long)

        val a = sin(deltaLat / 2) * sin(deltaLat / 2) +
                cos(lat1) * cos(lat2) *
                sin(deltaLong / 2) * sin(deltaLong / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return (EARTH_RADIUS_METERS * c).toFloat()
    }

}