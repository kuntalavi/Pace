package com.rk.pace.common.ut

import com.google.android.gms.maps.model.LatLng
import com.rk.pace.domain.model.RunPathPoint

object PathUt {

    fun RunPathPoint.toLatL(): LatLng {
        return LatLng(
            lat,
            long
        )
    }

    fun List<RunPathPoint>.toLatL(): List<LatLng> {
        return this.map { point ->
            point.toLatL()
        }
    }

    fun List<List<RunPathPoint>>.toList(): List<RunPathPoint> {
        val list = mutableListOf<RunPathPoint>()

        this.forEachIndexed { index, segment ->
            segment.forEachIndexed { i, point ->
                if (i == segment.lastIndex && index != lastIndex) {
                    list.add(
                        point.copy(
                            isPausePoint = true
                        )
                    )
                } else {
                    list.add(
                        point
                    )
                }
            }
        }
        return list
    }

    fun List<RunPathPoint>.toSegments(): List<List<RunPathPoint>> {
        val segments = mutableListOf<MutableList<RunPathPoint>>()
        var current = mutableListOf<RunPathPoint>()

        this.forEach { point ->
            current.add(
                point.copy(
                    isPausePoint = false
                )
            )

            if (point.isPausePoint) {
                segments.add(
                    current
                )
                current = mutableListOf()
            }
        }

        if (current.isNotEmpty()) {
            segments.add(
                current
            )
        }
        return segments
    }

}