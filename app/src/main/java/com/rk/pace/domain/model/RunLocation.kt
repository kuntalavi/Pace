package com.rk.pace.domain.model

data class RunLocation(
    val id: Long = 0,
    val runId: Long,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val accuracy: Float,
    val timestamp: Long,
    val speedMps: Float
)
