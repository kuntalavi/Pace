package com.rk.pace.domain.model

data class Run(
    val runId: Long = 0,
    val timestamp: Long,
    val durationM: Long,
    val distanceMeters: Float,
    val avgSpeedMps: Float,
    val maxSpeedMps: Float,
    val ePath: String
)
