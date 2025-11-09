package com.rk.pace

import android.app.Application
import com.google.android.gms.maps.MapsInitializer
import com.rk.pace.background.notification.RunTrackNotification
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var runTrackNotification: RunTrackNotification

    override fun onCreate() {
        super.onCreate()
        MapsInitializer.initialize(this)
        runTrackNotification.createNotificationChannel()
    }
}