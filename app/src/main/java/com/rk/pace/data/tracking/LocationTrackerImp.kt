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

    private var locationCallback: LocationTracker.LocationCallback? = null

    private val gLocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            locationCallback?.onLocationUpdate(
                p0.locations.map {
                    RunPathPoint(
                        timestamp = it.time,
                        lat = it.latitude,
                        long = it.longitude,
                        speedMps = it.speed,
                        accuracy = it.accuracy
                    )
                }
            )
        }
    }

    @SuppressLint("MissingPermission")
    override fun setCallback(locationCallback: LocationTracker.LocationCallback) {
        if (context.hasLocationPermission()) {
            this.locationCallback = locationCallback
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                gLocationCallback,
                Looper.getMainLooper()
            )
        }
    }

    override fun removeCallback() {
        this.locationCallback = null
        fusedLocationProviderClient.removeLocationUpdates(gLocationCallback)
    }
}