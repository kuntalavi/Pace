package com.rk.pace.domain.model

data class ActRunState(
    val isAct: Boolean = false,
    val distanceMeters: Int = 0,
    val durationM: Long = 0L,
    val path: List<RunPathPoint> = emptyList()
)