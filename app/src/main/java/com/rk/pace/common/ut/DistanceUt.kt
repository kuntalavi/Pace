package com.rk.pace.common.ut

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.rk.pace.domain.model.RunPathPoint
import kotlin.math.roundToInt

object DistanceUt {
    fun getDistanceBetweenRunPathPoints(
        runPathPoint1: LatLng,
        runPathPoint2: LatLng
    ): Int {
        //
        val distance = FloatArray(1)
        Location.distanceBetween(
            runPathPoint1.latitude,
            runPathPoint1.longitude,
            runPathPoint2.latitude,
            runPathPoint2.longitude,
            distance
        )
        return distance[0].roundToInt()
    }


    fun getTDistance(path: List<RunPathPoint>): Int {
//    var distance = 0
//    path.forEachIndexed { i,pathPoint ->
//
//    }
        return 0
    }
}