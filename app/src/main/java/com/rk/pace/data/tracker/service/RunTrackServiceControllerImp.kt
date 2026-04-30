package com.rk.pace.data.tracker.service

import android.content.Context
import android.content.Intent
import android.os.Build
import com.rk.pace.domain.tracker.RunTrackServiceController
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RunTrackServiceControllerImp @Inject constructor(
    @param:ApplicationContext private val context: Context
) : RunTrackServiceController {

    override fun startRunTrackService() {
        Intent(
            context,
            RunTrackService::class.java
        ).apply {
            action = RunTrackService.ACTION_START_SERVICE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(this)
            } else {
                context.startService(this)
            }
        }
    }

    override fun stopRunTrackService() {
        Intent(
            context,
            RunTrackService::class.java
        ).apply(
            context::stopService
        )
    }

}