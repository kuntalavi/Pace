package com.rk.pace.domain.mapper

import com.google.android.gms.maps.model.LatLng
import com.rk.pace.domain.model.RunPathPoint

object PathUt {
    fun RunPathPoint.toLatLng(): LatLng {
        return LatLng(
            lat,
            l
        )
    }
}