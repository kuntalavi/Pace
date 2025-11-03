package com.rk.pace.domain.model

data class Run(
    val runId: Long = 0,
    val startTime: Long,
    val endTime: Long,
    val distanceMeters: Float,
    val durationM: Long,
    val avgPace: Float,
    val maxPace: Float,
    val path: List<RunPathPoint>
)
