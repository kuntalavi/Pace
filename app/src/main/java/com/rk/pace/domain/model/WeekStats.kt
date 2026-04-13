package com.rk.pace.domain.model

data class WeekStats(
    val runs: Int,
    val distanceMeters: Float,
    val durationMilliseconds: Long,
    val avgSpeedMps: Float
)
