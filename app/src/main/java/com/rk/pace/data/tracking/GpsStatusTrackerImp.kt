package com.rk.pace.data.tracking

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import com.rk.pace.di.IoDispatcher
import com.rk.pace.domain.tracking.GpsStatusTracker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GpsStatusTrackerImp @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : GpsStatusTracker {
    private val locationManager =
        context.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

    override val isGpsEnabled: Flow<Boolean> = callbackFlow {
        val checkGps = {
            val status = locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER
            )
            trySend(
                status
            )
        }
        checkGps()

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
                    checkGps()
                }
            }
        }

        context.registerReceiver(
            receiver, IntentFilter(
                LocationManager.PROVIDERS_CHANGED_ACTION
            )
        )

        awaitClose {
            context.unregisterReceiver(receiver)
        }
    }
        .distinctUntilChanged()
        .conflate()
        .flowOn(ioDispatcher)
}