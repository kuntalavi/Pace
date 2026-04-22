package com.rk.pace.data.tracking

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.rk.pace.common.extension.hasPreciseForegroundLocationPermission
import com.rk.pace.di.ApplicationDefaultCoroutineScope
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.tracking.LocationTracker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationTrackerImp @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:ApplicationDefaultCoroutineScope private val scope: CoroutineScope,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val locationRequest: LocationRequest
) : LocationTracker {

    @SuppressLint("MissingPermission")
    override val locationFlow: Flow<RunPathPoint> = callbackFlow {
        var updatesRequested = false
        val callback = object : LocationCallback() {

            override fun onLocationResult(result: LocationResult) {
                result.locations.forEach { location ->
                    trySend(
                        RunPathPoint(
                            timestamp = location.time,
                            lat = location.latitude,
                            long = location.longitude,
                            speedMps = location.speed,
                            accuracy = location.accuracy
                        )
                    )
                }
            }

        }

        fun removeLocationUpdates() {
            if (updatesRequested) {
                fusedLocationProviderClient.removeLocationUpdates(callback)
                updatesRequested = false
            }
        }

        val job = launch {
            while (isActive) {
                val hasPermission = context.hasPreciseForegroundLocationPermission()

                if (hasPermission && !updatesRequested) {
                    try {
                        fusedLocationProviderClient.requestLocationUpdates(
                            locationRequest,
                            callback,
                            Looper.getMainLooper()
                        )
                        updatesRequested = true
                    } catch (e: SecurityException) {
                        Log.e("Location Request Error", e.message ?: "")
                        updatesRequested = false
                    }
                } else if (!hasPermission && updatesRequested) {
                    removeLocationUpdates()
                }

                delay(2000L)
            }
        }

        awaitClose {
            job.cancel()
            removeLocationUpdates()
        }
    }.shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000L),
        replay = 1
    )

}
