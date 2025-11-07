package com.rk.pace.data.tracking

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.rk.pace.common.extension.hasLocationPermission
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.tracking.LocationTracker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationTrackerImp @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val locationRequest: LocationRequest
) : LocationTracker {

    private var locationCback: LocationTracker.LocationCback? = null

    private val gLocationCback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            locationCback?.onLocationUpdate(
                p0.locations.map {
                    RunPathPoint(
                        timestamp = it.time,
                        lat = it.latitude,
                        l = it.longitude,
                        speedMps = it.speed
                    )
                }
            )
        }
    }

    @SuppressLint("MissingPermission")
    override fun setCback(locationCback: LocationTracker.LocationCback) {
        if (context.hasLocationPermission()) {
            this.locationCback = locationCback
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                gLocationCback,
                Looper.getMainLooper()
            )
        }
    }

    override fun removeCback() {
        this.locationCback = null
        fusedLocationProviderClient.removeLocationUpdates(gLocationCback)
    }
}