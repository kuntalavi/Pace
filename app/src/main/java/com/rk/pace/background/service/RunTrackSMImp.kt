package com.rk.pace.background.service

import android.content.Context
import android.content.Intent
import android.os.Build
import com.rk.pace.domain.tracking.RunTrackSM
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RunTrackSMImp @Inject constructor(
    @param:ApplicationContext private val context: Context
) : RunTrackSM {

    override fun startRunTrackS() {
        Intent(context, RunTrackService::class.java).apply {
            action = RunTrackService.ACTION_START_SERVICE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(this)
            } else {
                context.startService(this)
            }
        }
    }

    override fun stopRunTrackS() {
        Intent(context, RunTrackService::class.java).apply(context::stopService)
    }
}