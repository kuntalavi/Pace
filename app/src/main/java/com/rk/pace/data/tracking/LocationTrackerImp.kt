package com.rk.pace.data.tracking

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.rk.pace.common.extension.hasPreciseForegroundLocationPermission
import com.rk.pace.di.ActiveTrackLocationRequest
import com.rk.pace.di.ApplicationDefaultCoroutineScope
import com.rk.pace.di.PassiveLocationRequest
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.tracking.LocationTracker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationTrackerImp @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:ApplicationDefaultCoroutineScope private val scope: CoroutineScope,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @param:PassiveLocationRequest private val passiveLocationRequest: LocationRequest,
    @param:ActiveTrackLocationRequest private val activeTrackLocationRequest: LocationRequest
) : LocationTracker {

    val permission = context.hasPreciseForegroundLocationPermission()

    @SuppressLint("MissingPermission")
    override val passiveLocation: Flow<RunPathPoint?> = callbackFlow {
        if (!permission) {
            trySend(null)
        }

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    trySend(
                        location.toRunPathPoint()
                    )
                }
            }
        }

        if (permission) {
            fusedLocationProviderClient.requestLocationUpdates(
                passiveLocationRequest,
                callback,
                Looper.getMainLooper()
            )
        }

        awaitClose {
            fusedLocationProviderClient.removeLocationUpdates(callback)
        }
    }.shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000L),
        replay = 1
    )

    @SuppressLint("MissingPermission")
    override val activeTrackLocation: Flow<RunPathPoint> = callbackFlow {
        val callback = object : LocationCallback() {

            override fun onLocationResult(result: LocationResult) {
                result.locations.forEach { location ->
                    trySend(
                        location.toRunPathPoint()
                    )
                }
            }

        }

        if (permission) {
            fusedLocationProviderClient.requestLocationUpdates(
                activeTrackLocationRequest,
                callback,
                Looper.getMainLooper()
            )
        }

        awaitClose {
            fusedLocationProviderClient.removeLocationUpdates(callback)
        }
    }.shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000L),
        replay = 1
    )

}

private fun android.location.Location.toRunPathPoint(): RunPathPoint {
    return RunPathPoint(
        timestamp = time,
        lat = latitude,
        long = longitude,
        speedMps = speed,
        accuracy = accuracy
    )
}
