package com.rk.pace.background.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.rk.pace.MainActivity
import com.rk.pace.R
import com.rk.pace.background.service.RunTrackService
import com.rk.pace.presentation.Route
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RunTrackNotification @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    companion object {
        private const val RUN_TRACK_NOTIFICATION_CHANNEL_ID = "r_track_notification"
        private const val RUN_TRACK_NOTIFICATION_CHANNEL_NAME = "R Track"
        const val RUN_TRACK_NOTIFICATION_ID = 3
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val intentToRunScreen = TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(
            Intent(
                Intent.ACTION_VIEW,
                Route.Root.Run.runUriPattern.toUri(),
                context,
                MainActivity::class.java
            )
        )
        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    private val baseNotification
        get() = NotificationCompat.Builder(
            context,
            RUN_TRACK_NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.run_24px)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentTitle("Time")
            .setContentText("00:00:00")
            .setContentIntent(intentToRunScreen)

    private fun getNotificationAction(): NotificationCompat.Action {
        return NotificationCompat.Action(
            R.drawable.ic_launcher_background,
            "Pause",
            PendingIntent.getService(
                context,
                43,
                Intent(
                    context,
                    RunTrackService::class.java,
                ).apply {
                    action = RunTrackService.ACTION_PAUSE_SERVICE
                },
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT

            )
        )
    }

    fun updateNotification(durationInMillis: Long) {
        val notification = baseNotification
            .setContentText("$durationInMillis")
            .clearActions()
            .addAction(getNotificationAction())
            .build()

        notificationManager.notify(RUN_TRACK_NOTIFICATION_ID, notification)
    }

    fun removeNotification() {
        notificationManager.cancel(RUN_TRACK_NOTIFICATION_ID)
    }

    fun getBaseNotification() = baseNotification.build()

    fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val notificationChannel = NotificationChannel(
            RUN_TRACK_NOTIFICATION_CHANNEL_ID,
            RUN_TRACK_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }
}