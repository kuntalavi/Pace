package com.rk.pace.domain.model

data class RunState(
    val isAct: Boolean = false,
    val paused: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val durationMilliseconds: Long = 0L,
    val distanceMeters: Float = 0f,
    val avgSpeedMps: Float = 0f,
    val speedMps: Float = 0f,
    val segments: List<List<RunPathPoint>> = listOf(emptyList())
)