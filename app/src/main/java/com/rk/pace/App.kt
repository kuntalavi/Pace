package com.rk.pace

import android.app.Application
import com.google.android.gms.maps.MapsInitializer
import com.rk.pace.background.notification.RunTrackingNotification
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var runTrackingNotification: RunTrackingNotification

    override fun onCreate() {
        super.onCreate()
        MapsInitializer.initialize(this)
        runTrackingNotification.createNotificationChannel()
    }
}