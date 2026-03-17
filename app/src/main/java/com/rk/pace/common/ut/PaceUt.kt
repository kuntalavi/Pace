package com.rk.pace.common.ut

import java.util.Locale

object PaceUt {

    fun getPace(speedMps: Float): String {
        if (speedMps <= 0) return "-"

        val paceM = 16.6667 / speedMps
        val minutes = paceM.toInt()
        val seconds = ((paceM - minutes) * 60).toInt()

        return String.format(Locale.getDefault(), "%d:%02d /Km", minutes, seconds)
    }

    fun formatPace(paceSeconds: Float): String {
        val duration = paceSeconds.toInt()
        val minutes = duration / 60
        val seconds = duration % 60

        return "%d:%02d".format(minutes, seconds)
    }

}