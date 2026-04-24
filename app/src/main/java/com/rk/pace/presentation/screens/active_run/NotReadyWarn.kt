package com.rk.pace.presentation.screens.active_run

enum class NotReadyWarn(
    val message: String
) {
    LOCATION_PERMISSION_NOT_GRANTED("location"),
    GPS_OFF("gps")
}