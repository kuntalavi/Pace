package com.rk.pace.background.service

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.rk.pace.background.notification.RunTrackingNotification
import com.rk.pace.domain.tracking.TrackerManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

class RunTrackingService : LifecycleService() {
    companion object {
        const val ACTION_PAUSE_TRACKING = "action_pause_tracking"
        const val ACTION_RESUME_TRACKING = "action_resume_tracking"
        const val ACTION_START_SERVICE = "action_start_service"
    }

    @Inject
    lateinit var trackerManager: TrackerManager

    @Inject
    lateinit var notification: RunTrackingNotification
    private var job: Job? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.action) {
            ACTION_PAUSE_TRACKING -> trackerManager.pause()
            ACTION_RESUME_TRACKING -> trackerManager.startResume()
            ACTION_START_SERVICE -> {
                startForeground(
                    RunTrackingNotification.TRACKING_NOTIFICATION_ID,
                    notification.getBaseNotification()
                )

                if (job == null) {
                    job = combine(
                        trackerManager.actRunState,
                        trackerManager.durationInM
                    ) { runState, duration ->
                        notification.updateNotification(
                            durationInMillis = duration
                        )
                    }.launchIn(lifecycleScope)
                }
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        notification.removeNotification()

        job?.cancel()
        job = null
    }
}