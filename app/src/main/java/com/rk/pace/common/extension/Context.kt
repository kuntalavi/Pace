package com.rk.pace.common.extension

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

fun Context.restartApp() {
    val intent = this.packageManager
        .getLaunchIntentForPackage(this.packageName)

    intent?.addFlags(
        Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
    )
    this.startActivity(intent)
}

fun Activity.openAppSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts(
            "package",
            packageName,
            null
        )
    )

    startActivity(intent)
}

fun Activity.openGpsSettings(){
    val intent = Intent(
        Settings.ACTION_LOCATION_SOURCE_SETTINGS
    )

    startActivity(intent)
}

fun Context.hasPermission(permission: String) =
    ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Context.hasPostNotificationPermission() =
    ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED

fun Context.hasPreciseForegroundLocationPermission() =
    (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            )
            &&
            (
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                    )
