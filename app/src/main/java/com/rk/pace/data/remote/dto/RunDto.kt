package com.rk.pace.data.remote.dto

data class RunDto(
    val runId: String = "",
    val userId: String = "",
    val timestamp: Long = 0L,
    val durationMilliseconds: Long = 0L,
    val distanceMeters: Float = 0f,
    val avgSpeedMps: Float = 0f,
    val encodedPath: List<String> = emptyList(),
    val pathPoints: List<RunPathPointDto> = emptyList(),

    val title: String = "",
    val likes: Int = 0,
    val likedBy: List<String> = emptyList()
)
