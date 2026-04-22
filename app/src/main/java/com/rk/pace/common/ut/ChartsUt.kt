package com.rk.pace.common.ut

import com.rk.pace.common.ut.DistanceUt.getDistance
import com.rk.pace.domain.model.PacePoint
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.model.Split

object ChartsUt {

    /*
    * Empty Data Scenes ->
    * path.size < 2
    * p2.accuracy > 20f
    * dx < 3f || dt <= 0f
    * sensor speed <= 0f
    * pace <= 2f && pace >= 12f
    * */

    fun buildPaceChartData(path: List<RunPathPoint>): List<PacePoint> {
        val raw = buildRawPacePoints(path)
        val filtered = filterPacePoints(raw)
        val sampled = sampleByDistance(filtered, 0.02f) // 20 METERS
        val smoothed = smoothEMA(sampled, alpha = 0.2f)
        return smoothed
    }

    fun buildRawPacePoints(path: List<RunPathPoint>): List<PacePoint> {
        if (path.size < 2) return emptyList()

        val result = mutableListOf<PacePoint>()
        var tDistance = 0f

        for (i in 1 until path.size) {
            val p1 = path[i - 1]
            val p2 = path[i]

            // Break
            if (p1.pausePoint || p2.pausePoint) continue
            if (p2.accuracy > 20f) continue

            val dx = getDistance(p1, p2) // meters
            val dt = (p2.timestamp - p1.timestamp) / 1000f // seconds

            if (dx < 3f || dt <= 0f) continue

            // Prefer sensor speed
            val speed = if (p2.speedMps > 0f) p2.speedMps else dx / dt

            if (speed <= 0f) continue

            val pace = (1000f / speed) / 60f // min/km

            tDistance += dx

            result.add(
                PacePoint(
                    distanceKm = tDistance / 1000f,
                    paceMinPerKm = pace
                )
            )
        }

        return result
    }

    fun filterPacePoints(data: List<PacePoint>): List<PacePoint> {
        return data.filter {
            it.paceMinPerKm in 1f..15f
        }
    }

    fun sampleByDistance(
        data: List<PacePoint>,
        stepKm: Float
    ): List<PacePoint> {

        if (data.isEmpty()) return emptyList()

        val result = mutableListOf<PacePoint>()
        var lastDistance = 0f

        for (point in data) {
            if (point.distanceKm - lastDistance >= stepKm) {
                result.add(point)
                lastDistance = point.distanceKm
            }
        }

        return result
    }

    fun smoothEMA(
        data: List<PacePoint>,
        alpha: Float = 0.2f
    ): List<PacePoint> {

        if (data.isEmpty()) return emptyList()

        val result = mutableListOf<PacePoint>()
        var prev = data.first().paceMinPerKm

        for (point in data) {
            val smoothed = alpha * point.paceMinPerKm + (1 - alpha) * prev

            result.add(
                point.copy(
                    paceMinPerKm = smoothed
                )
            )

            prev = smoothed
        }

        return result
    }

//    fun getPaceMinutes(path: List<List<RunPathPoint>>): List<Float>{
//        val paceMinutes = mutableListOf<Float>()
//
//        for (segment in path){
//            for (point in segment){
//                paceMinutes.add(point.speedMps)
//            }
//        }
//    }

    /**
     * Error
     */

    fun buildSplitChartData(
        path: List<List<RunPathPoint>>,
        splitDistanceMeters: Float = 1000f
    ): List<Split> {

        val splits = mutableListOf<Split>()

        var cumulativeDistance = 0f
        var splitStartTime: Long? = null
        var nextSplitDistance = splitDistanceMeters
        var splitIndex = 1

        var prev: RunPathPoint? = null

        for (segment in path) {
            for (point in segment) {

                if (prev == null) {
                    prev = point
                    splitStartTime = point.timestamp
                    continue
                }

                val distance = getDistance(prev, point)
                val timeDelta = point.timestamp - prev.timestamp

                val previousDistance = cumulativeDistance
                cumulativeDistance += distance

                while (cumulativeDistance >= nextSplitDistance) {

                    val distanceNeeded =
                        nextSplitDistance - previousDistance

                    val ratio = distanceNeeded / distance

                    val interpolatedTime =
                        prev.timestamp + (timeDelta * ratio).toLong()

                    val splitDuration =
                        interpolatedTime - splitStartTime!!

                    val paceSeconds =
                        (splitDuration / 1000f) / (splitDistanceMeters / 1000f)

                    splits.add(
                        Split(
                            index = splitIndex,
                            durationMilliseconds = splitDuration,
                            paceSeconds = paceSeconds
                        )
                    )

                    splitIndex++
                    splitStartTime = interpolatedTime
                    nextSplitDistance += splitDistanceMeters
                }

                prev = point
            }
        }

        val lastPoint = path.lastOrNull()?.lastOrNull()

        if (lastPoint != null && splitStartTime != null) {

            val remainingDistance =
                cumulativeDistance - (nextSplitDistance - splitDistanceMeters)

            if (remainingDistance > 0f) {

                val splitDuration =
                    lastPoint.timestamp - splitStartTime

                val paceSeconds =
                    (splitDuration / 1000f) / (remainingDistance / 1000f)

                splits.add(
                    Split(
                        index = splitIndex,
                        distanceMeters = remainingDistance,
                        durationMilliseconds = splitDuration,
                        paceSeconds = paceSeconds
                    )
                )
            }
        }

        return splits
    }
}