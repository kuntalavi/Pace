package com.rk.pace.presentation.ut

import com.rk.pace.domain.ut.DistanceUt
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

            val dx = DistanceUt.getDistance(p1, p2) // meters
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
        return data
//        return data.filter {
//            it.paceMinPerKm in 1..15f
//        }
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
     * ChangesSaveError
     */

    fun buildSplitChartData(
        path: List<List<RunPathPoint>>,
        splitDistanceMeters: Float = 1000f
    ): List<Split> {
        val splits = mutableListOf<Split>()

        var splitIndex = 1
        var currentSplitTime = 0L
        var currentSplitDistance = 0f

        for (segment in path) {
            // CRITICAL: This MUST be inside the segment loop to ignore pauses!
            var prev: RunPathPoint? = null

            for (point in segment) {
                if (prev == null) {
                    prev = point
                    continue
                }

                val distance = DistanceUt.getDistance(prev, point)
                val timeDelta = point.timestamp - prev.timestamp

//                // --- 🛑 ANOMALY FILTER (The Zig-Zag Killer) ---
//                if (timeDelta <= 0L) continue // Ignore identical or backward timestamps

                val speedMps = distance / (timeDelta / 1000f)

//                // Human sprint record is ~10.4 m/s.
//                // If GPS says you moved faster than 15 m/s (54 km/h), it's a glitch. SKIP IT.
//                if (speedMps > 15f) continue
//                // ----------------------------------------------

                var remainingSegDistance = distance
                var remainingSegTime = timeDelta

                // Accumulate chunks into splits
                while (currentSplitDistance + remainingSegDistance >= splitDistanceMeters) {
                    val distanceNeeded = splitDistanceMeters - currentSplitDistance
                    val ratio = if (remainingSegDistance > 0) distanceNeeded / remainingSegDistance else 0f
                    val timeNeeded = (remainingSegTime * ratio).toLong()

                    currentSplitTime += timeNeeded
                    currentSplitDistance += distanceNeeded

                    val paceSeconds = (currentSplitTime / 1000f) / (splitDistanceMeters / 1000f)

                    splits.add(
                        Split(
                            index = splitIndex,
                            durationMilliseconds = currentSplitTime,
                            paceSeconds = paceSeconds
                        )
                    )

                    splitIndex++

                    remainingSegDistance -= distanceNeeded
                    remainingSegTime -= timeNeeded
                    currentSplitTime = 0L
                    currentSplitDistance = 0f
                }

                currentSplitDistance += remainingSegDistance
                currentSplitTime += remainingSegTime

                // Only update 'prev' if the point passed the speed filter
                prev = point
            }
        }

        // Add the final incomplete split (e.g., the 0.37 km at the end)
        if (currentSplitDistance > 0f) {
            val paceSeconds = (currentSplitTime / 1000f) / (currentSplitDistance / 1000f)
            splits.add(
                Split(
                    index = splitIndex,
                    distanceMeters = currentSplitDistance,
                    durationMilliseconds = currentSplitTime,
                    paceSeconds = paceSeconds
                )
            )
        }

        return splits
    }
}