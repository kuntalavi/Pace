package com.rk.pace.domain.model

import com.google.android.gms.maps.model.LatLng

data class RunState(
    val isAct: Boolean = false,
    val distanceInMeters: Float = 0f,
    val durationInM: Long = 0L,
    val speedMps: Float = 0f,
    val path: List<LatLng> = emptyList(),
    val runPathPoints: List<RunPathPoint> = emptyList()
)