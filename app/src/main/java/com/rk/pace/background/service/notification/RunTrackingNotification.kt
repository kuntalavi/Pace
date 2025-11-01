package com.rk.pace.background.service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.rk.pace.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RunTrackingNotification @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    companion object {
        private const val TRACKING_NOTIFICATION_CHANNEL_ID = "track_notification"
        private const val TRACKING_NOTIFICATION_CHANNEL_NAME = "Run Status"
        const val TRACKING_NOTIFICATION_ID = 3
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val baseNotification
        get() = NotificationCompat.Builder(
            context,
            TRACKING_NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_run)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentTitle("Time")
            .setContentText("00:00:00")

    fun removeNotification() {
        notificationManager.cancel(TRACKING_NOTIFICATION_ID)
    }

    fun getBaseNotification() = baseNotification.build()

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return

        val notificationChannel = NotificationChannel(
            TRACKING_NOTIFICATION_CHANNEL_ID,
            TRACKING_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }
}