package com.rk.pace.common.extension

import java.util.Locale

fun Float.formatDistance(): String {
    return if (this >= 1000f){
        String.format(Locale.US, "%.1f km", this / 1000f)
    }else String.format(Locale.US, "%.0f m", this)
}

fun Long.formatTime(): String {
    val seconds = this.coerceAtLeast(0L) / (1000L)
    val hours = seconds / 3600L
    val minutes = (seconds % 3600L) / (60L)
    val secs = seconds % 60L

    return if (hours > 0L) {
        String.format(Locale.US, "%dh %02dm %02ds", hours, minutes, secs)
    } else if (minutes > 0L) {
        String.format(Locale.US, "%d m %02d s", minutes, secs)
    } else {
        String.format(Locale.US, "%02d s", secs)
    }
}