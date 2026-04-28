package com.rk.pace.presentation.ut

object FormatUt {

    /*
    * FORMAT UT FOR DISTANCE, DURATION, AVG SPEED/PACE
    * */

    fun formatDistance(meters: Float): String {
        if (meters == 0f) return "-"
        val km = "%.2f".format(meters / 1000f)

        return if (km[2] == '0' && km[0] == '0') "0"
        else km
    }

    fun formatPace(speedMps: Float): String {
        if (speedMps <= 0f) return "-"

        val paceMpkm = 1000 / (speedMps * 60)

        val minutes = paceMpkm.toInt()
        val seconds = ((paceMpkm - minutes) * 60).toInt()

        return "%d:%02d".format(minutes, seconds)
    }

    fun formatDuration(ms: Long): String {
        if (ms == 0L) return "-"

        val s = ms / 1000
        val hours = s / 3600
        val minutes = (s % 3600) / 60
        val seconds = s % 60

        return if (hours > 0) {
            "%d:%02d:%02d".format(hours, minutes, seconds)
        } else {
            "%02d:%02d".format(minutes, seconds)
        }
    }

}