package com.rk.pace.domain.model

import java.util.UUID

data class Run(
    val runId: String = UUID.randomUUID().toString(),
    val userId: String,
    val timestamp: Long,
    val durationMilliseconds: Long,
    val distanceMeters: Float,
    val avgSpeedMps: Float,
    val encodedPath: List<String>,

    val title: String,
    val likes: Int,
    val likedBy: List<String>
)
