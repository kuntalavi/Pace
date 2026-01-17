package com.rk.pace.common.ut

import android.net.Uri
import com.rk.pace.BuildConfig
import com.rk.pace.domain.model.RunPathPoint
import kotlin.math.roundToInt

object MapUt {

    private const val MAPBOX_ACCESS_TOKEN = BuildConfig.MAPBOX_ACCESS_TOKEN
    private const val BASE_URL = "https://api.mapbox.com/styles/v1/mapbox/outdoors-v12/static"

    fun encodeSegments(segments: List<List<RunPathPoint>>): List<String> {
        return segments.map { segment ->
            encode(segment)
        }
    }

    fun encode(points: List<RunPathPoint>): String {
        val str = StringBuilder()
        var oLat = 0
        var oLong = 0

        for (point in points) {
            val lat = (point.lat * 100000.0).roundToInt()
            val long = (point.long * 100000.0).roundToInt()

            encodeValue(lat - oLat, str)
            encodeValue(long - oLong, str)

            oLat = lat
            oLong = long
        }
        return str.toString()
    }

    fun encodeValue(value: Int, str: StringBuilder) {
        var v = value shl 1
        if (value < 0) v = v.inv()

        while (v >= 0x20) {
            val chunk = (v and 0x1f) or 0x20
            str.append((chunk + 63).toChar())
            v = v ushr 5
        }

        str.append((v + 63).toChar())
    }

    fun buildStaticMapUrl(
        encodedPath: List<String>,
        width: Int = 600,
        height: Int = 300
    ): String {
        val overlays = encodedPath.joinToString(",") { encoded ->
            (if (encoded.isNotBlank()) {
                val safeEncoded = Uri.encode(encoded)
                "path-5+f44336($safeEncoded)"
            } else null).toString()
        }
        if (overlays.isEmpty()) return ""

        val attribution = "attribution=false"
        val logo = "logo=false"

        val url =  "$BASE_URL/$overlays/auto/${width}x${height}@2x?${attribution}&${logo}&access_token=${MAPBOX_ACCESS_TOKEN}"
        return url
    }

}