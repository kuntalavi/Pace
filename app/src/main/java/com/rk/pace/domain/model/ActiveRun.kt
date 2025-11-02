package com.rk.pace.domain.model

data class ActiveRun(
    val isActive: Boolean = false,
    val distanceInMeters: Int = 0,
    val durationInMillis: Long = 0L
)