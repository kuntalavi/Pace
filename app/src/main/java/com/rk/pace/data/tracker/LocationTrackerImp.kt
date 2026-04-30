package com.rk.pace.data.tracker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.rk.pace.data.ut.hasPreciseForegroundLocationPermission
import com.rk.pace.di.ActiveTrackLocationRequest
import com.rk.pace.di.PassiveLocationRequest
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.tracker.LocationTracker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

@SuppressLint("MissingPermission")
@Singleton
class LocationTrackerImp @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @param:PassiveLocationRequest private val passiveLocationRequest: LocationRequest,
    @param:ActiveTrackLocationRequest private val activeTrackLocationRequest: LocationRequest
) : LocationTracker {

    override val passiveLocation: Flow<RunPathPoint?> =
        getLocationFlow(
            locationRequest = passiveLocationRequest,
            extractLocation = { result ->
                listOfNotNull(result.lastLocation?.toRunPathPoint())
            }
        )

    override val activeTrackLocation: Flow<RunPathPoint> =
        getLocationFlow(
            locationRequest = activeTrackLocationRequest,
            extractLocation = { result ->
                result.locations.map { location ->
                    location.toRunPathPoint()
                }
            }
        )

    private fun getLocationFlow(
        locationRequest: LocationRequest,
        extractLocation: (LocationResult) -> List<RunPathPoint>
    ): Flow<RunPathPoint> =
        callbackFlow {
            if (!context.hasPreciseForegroundLocationPermission()) {
                throw SecurityException("Location Permission Not Granted")
            }

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    extractLocation(result).forEach { trySend(it) }
                }
            }

            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                callback,
                Looper.getMainLooper()
            )

            fusedLocationProviderClient.lastLocation.addOnSuccessListener { result ->
                result?.let { location ->
                    trySend(
                        location.toRunPathPoint()
                    )
                }
            }

            awaitClose {
                fusedLocationProviderClient.removeLocationUpdates(callback)
            }
        }
            .distinctUntilChanged()

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
