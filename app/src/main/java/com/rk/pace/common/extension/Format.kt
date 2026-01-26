package com.rk.pace.common.extension

import java.util.Locale

fun Float.formatDistance(): String {
    return if (this == 0f) {
        String.format(Locale.getDefault(), "0 Km")
    } else if (this < 100f) {
        String.format(Locale.getDefault(), "%.0f m", this)
    } else if (this < 1000f) {
        String.format(Locale.getDefault(), "0.%.0f Km", this.toInt() / 10f)
    } else String.format(Locale.getDefault(), "%.2f Km", this.toInt() / 1000f)
}

fun Long.formatTime(): String {
    val seconds = this.coerceAtLeast(0L) / (1000L)
    val hours = seconds / 3600L
    val minutes = (seconds % 3600L) / (60L)
    val secs = seconds % 60L

    return if (hours > 0L) {
        String.format(Locale.getDefault(), "%dh %02dm %02ds", hours, minutes, secs)
    } else if (minutes > 0L) {
        String.format(Locale.getDefault(), "%d:%02d", minutes, secs)
    } else {
        String.format(Locale.getDefault(), "00:%02d", secs)
    }
}
