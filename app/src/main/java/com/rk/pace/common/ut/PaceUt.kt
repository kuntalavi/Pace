package com.rk.pace.common.ut

import java.util.Locale

object PaceUt {

    fun getPace(speedMps: Float): String {
        if (speedMps <= 0) return "-"

        val paceM = 16.6667 / speedMps
        val minutes = paceM.toInt()
        val seconds = ((paceM - minutes) * 60).toInt()

        return String.format(Locale.US, "%d:%02d /km", minutes, seconds)
    }

}