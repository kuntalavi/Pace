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
import com.rk.pace.presentation.navigation.Route
import com.rk.pace.presentation.ut.FormatUt.formatDistance
import com.rk.pace.presentation.ut.FormatUt.formatDuration
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RunTrackNotification @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    companion object {
        private const val RUN_TRACK_NOTIFICATION_CHANNEL_ID = "track_notification"
        private const val RUN_TRACK_NOTIFICATION_CHANNEL_NAME = "Run Track"
        const val RUN_TRACK_NOTIFICATION_ID = 3
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val intentToRunScreen = TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(
            Intent(
                Intent.ACTION_VIEW,
                Route.ActiveRun.Run.uri.toUri(),
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
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentIntent(intentToRunScreen)
            .setSilent(true)

    private fun getPauseResumeAction(paused: Boolean): NotificationCompat.Action {
        val actionText = if (paused) "Resume" else "Pause"

        val serviceAction = if (paused) {
            RunTrackService.ACTION_RESUME_SERVICE
        } else {
            RunTrackService.ACTION_PAUSE_SERVICE
        }

        val intent = Intent(context, RunTrackService::class.java).apply {
            action = serviceAction
        }
        val pendingIntent = PendingIntent.getService(
            context,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Action(
            R.drawable.ic_launcher_foreground,
            actionText,
            pendingIntent
        )
    }

    fun updateNotification(
        paused: Boolean,
        distanceMeters: Float,
        durationMilliseconds: Long
    ) {
        var distance = formatDistance(distanceMeters)
        val time = formatDuration(durationMilliseconds)

        if (distance == "-") distance = "0"

        val notification = baseNotification
            .setContentTitle("$distance km")
            .setContentText(time)
            .clearActions()
            .addAction(getPauseResumeAction(paused))
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