package com.rk.pace.domain.model

data class Split(
    val index: Int,
    val distanceMeters: Float = 1000f,
    val durationMilliseconds: Long,
    val paceSeconds: Float
)
