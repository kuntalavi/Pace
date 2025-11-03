package com.rk.pace.common.ut

import android.location.Location
import com.rk.pace.domain.model.RunPathPoint
import kotlin.math.roundToInt

object DistanceUt {
    fun getDistanceBetweenRunPathPoints(
        runPathPoint1: RunPathPoint,
        runPathPoint2: RunPathPoint
    ): Int {
//        if () point exist check
        val distance = FloatArray(1)
        Location.distanceBetween(
            runPathPoint1.lat,
            runPathPoint1.l,
            runPathPoint2.lat,
            runPathPoint2.l,
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