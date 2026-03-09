package com.rk.pace.data.tracking

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import com.rk.pace.domain.tracking.GpsStatusTracker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GpsStatusTrackerImp @Inject constructor(
    @param:ApplicationContext private val context: Context
) : GpsStatusTracker {
    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    override val isGpsEnabled: Flow<Boolean> = callbackFlow {
        val checkStatus = {
            val isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            trySend(isEnabled)
        }
        checkStatus()

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
                    checkStatus()
                }
            }
        }

        context.registerReceiver(receiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))

        awaitClose {
            context.unregisterReceiver(receiver)
        }
    }
}