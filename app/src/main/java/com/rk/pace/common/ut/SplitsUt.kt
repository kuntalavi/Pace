package com.rk.pace.common.ut

import com.rk.pace.common.ut.DistanceUt.getDistance
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.model.Split

object SplitsUt {

    fun calculateSplits(
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