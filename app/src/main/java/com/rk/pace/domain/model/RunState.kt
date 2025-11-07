package com.rk.pace.domain.model

data class RunState(
    val isAct: Boolean = false,
    val paused: Boolean = false,
    val timestamp: Long = 0L,
    val durationInM: Long = 0L,
    val distanceInMeters: Float = 0f,
    val speedMps: Float = 0f,
    val segments: List<List<RunPathPoint>> = emptyList()
)