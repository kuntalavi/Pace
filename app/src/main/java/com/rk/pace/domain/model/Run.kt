package com.rk.pace.domain.model

data class Run(
    val id: Long = 0,
    val startTime: Long,
    val endTime: Long,
    val distanceMeters: Float,
    val durationInMillis: Long,
    val avgPace: Float,
    val maxPace: Float,
    val elevationGainMeters: Float
)
