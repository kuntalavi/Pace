package com.rk.pace.background.service

import android.content.Context
import android.content.Intent
import android.os.Build
import com.rk.pace.domain.tracking.BackgroundTracker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BackgroundTrackerImp @Inject constructor(
    @param:ApplicationContext private val context: Context
) : BackgroundTracker {
    override fun startBackgroundTracking() {
        Intent(context, RunTrackingService::class.java).apply {
            action = RunTrackingService.ACTION_START_SERVICE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(this)
            } else {
                context.startService(this)
            }
        }
    }

    override fun stopBackgroundTracking() {
        Intent(context, RunTrackingService::class.java).apply(context::stopService)
    }
}