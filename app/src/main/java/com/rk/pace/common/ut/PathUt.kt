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
            segment.forEach {
                list.add(it)
            }
            if (index != this.lastIndex) {
                list.add(
                    RunPathPoint(
                        lat = segment.last().lat,
                        long = segment.last().long,
                        timestamp = segment.last().timestamp,
                        speedMps = segment.last().speedMps,
                        isPausePoint = true
                    )
                )
            }
        }
        return list
    }

    fun List<RunPathPoint>.toSegments(): List<List<RunPathPoint>> {
        val segments = mutableListOf<MutableList<RunPathPoint>>()
        var cSegment = mutableListOf<RunPathPoint>()
        this.forEach { point ->
            if (point.isPausePoint) {
                if (cSegment.isNotEmpty()) {
                    segments.add(cSegment)
                    cSegment = mutableListOf()
                }
            } else {
                cSegment.add(point)
            }
        }
        if (cSegment.isNotEmpty()) {
            segments.add(cSegment)
        }
        return segments
    }

}