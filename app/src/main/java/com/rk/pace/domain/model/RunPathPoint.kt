package com.rk.pace.domain.model

data class RunPathPoint(
    val timestamp: Long = 0L,
    val lat: Double,
    val long: Double,
    val speedMps: Float = 0f,
    val isPausePoint: Boolean = false,
    val accuracy: Float = 0f
)
