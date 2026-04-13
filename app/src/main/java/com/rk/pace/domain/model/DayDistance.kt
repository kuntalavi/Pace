package com.rk.pace.domain.model

data class DayDistance(
    val day: String,
    val distanceMeters: Float,
    val isToday: Boolean = false
)
