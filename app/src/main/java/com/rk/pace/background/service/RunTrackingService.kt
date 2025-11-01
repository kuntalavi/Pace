package com.rk.pace.background.service

import android.content.Intent
import androidx.lifecycle.LifecycleService
import com.rk.pace.background.service.notification.RunTrackingNotification
import javax.inject.Inject

class RunTrackingService : LifecycleService() {
    companion object {
        const val ACTION_PAUSE_TRACKING = "action_pause_tracking"
        const val ACTION_RESUME_TRACKING = "action_resume_tracking"
        const val ACTION_START_SERVICE = "action_start_service"
    }

    @Inject
    lateinit var notification: RunTrackingNotification

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.action) {
            ACTION_PAUSE_TRACKING -> {}
            ACTION_RESUME_TRACKING -> {}
            ACTION_START_SERVICE -> {
                startForeground(
                    RunTrackingNotification.TRACKING_NOTIFICATION_ID,
                    notification.getBaseNotification()
                )
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        notification.removeNotification()
    }
}