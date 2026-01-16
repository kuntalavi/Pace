package com.rk.pace.data.remote.dto

data class RunPathPointDto(
    val timestamp: Long = 0L,
    val lat: Double = 0.0,
    val long: Double = 0.0,
    val speedMps: Float = 0f,
    val isPausePoint: Boolean = false
)
