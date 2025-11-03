package com.rk.pace.domain.model

import com.google.android.gms.maps.model.LatLng

data class RunPath(
    val runId: Long,
    val path: List<LatLng>
)
