package com.rk.pace.common.ut

import com.google.android.gms.maps.model.LatLng
import com.rk.pace.domain.model.RunPathPoint

object PathUt {

//    fun encodePath(path: List<LatLng>): String {
//        val r = StringBuilder()
//        var lastLat = 0
//        var lastLng = 0
//
//        path.forEach { point ->
//            val lat = (point.latitude * 1e5).toInt()
//            val lng = (point.longitude * 1e5).toInt()
//            val dLat = lat - lastLat
//            val dLng = lng - lastLng
//
//            listOf(dLat, dLng).forEach { coord ->
//                var v = coord shl 1
//                if (coord < 0) v = v.inv()
//                while (v >= 0x20) {
//                    r.append((0x20 or (v and 0x1f)) + 63)
//                    v = v shr 5
//                }
//                r.append(v + 63)
//            }
//
//            lastLat = lat
//            lastLng = lng
//        }
//
//        return r.toString()
//    }
//
//    fun decodePath(path: String): List<LatLng> {
//        val r = ArrayList<LatLng>()
//        var index = 0
//        val length = path.length
//        var lat = 0
//        var lng = 0
//
//        while (index < length) {
//            var b: Int
//            var shift = 0
//            var result = 0
//            do {
//                b = path[index++].code - 63
//                result = result or (b and 0x1f shl shift)
//                shift += 5
//            } while (b >= 0x20)
//            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
//            lat += dlat
//
//            shift = 0
//            result = 0
//            do {
//                b = path[index++].code - 63
//                result = result or (b and 0x1f shl shift)
//                shift += 5
//            } while (b >= 0x20)
//            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
//            lng += dlng
//
//            r.add(LatLng(lat / 1E5, lng / 1E5))
//        }
//
//        return r
//    }

    fun RunPathPoint.toLatL(): LatLng {
        return LatLng(
            lat,
            l
        )
    }

    fun List<RunPathPoint>.toLatL(): List<LatLng> {
        return this.map { point ->
            point.toLatL()
        }
    }

//    fun List<List<RunPathPoint>>.toLatL(): List<LatLng> {
//        val latL = mutableListOf<LatLng>()
//        this.forEach { segment ->
//            segment.forEach { point ->
//                latL.add(LatLng(point.lat, point.l))
//            }
//        }
//        return latL
//    }

}