package com.rk.pace.background.service

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.rk.pace.background.notification.RunTrackNotification
import com.rk.pace.domain.tracking.TrackerManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RunTrackService : LifecycleService() {
    companion object {
        const val ACTION_PAUSE_SERVICE = "action_pause_service"
        const val ACTION_RESUME_SERVICE = "action_resume_service"
        const val ACTION_START_SERVICE = "action_start_service"
    }

    @Inject
    lateinit var trackerManager: TrackerManager

    @Inject
    lateinit var notification: RunTrackNotification

    private var job: Job? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.action) {
            ACTION_PAUSE_SERVICE -> trackerManager.pause()
            ACTION_RESUME_SERVICE -> trackerManager.resume()
            ACTION_START_SERVICE -> {
                val baseNotification = notification.getBaseNotification()
                Log.d("RunTrackingNotification", "Notification built: $baseNotification")
                startForeground(
                    RunTrackNotification.RUN_TRACK_NOTIFICATION_ID,
                    baseNotification
                )
                Log.d("RunTrackingService", "onStartCommand called with action: ${intent.action}")

                if (job == null) {
                    job = lifecycleScope.launch {
                        trackerManager.runState.collect { runState ->
                            notification.updateNotification(
                                durationMilliseconds = runState.durationMilliseconds
                            )
                        }
                    }
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