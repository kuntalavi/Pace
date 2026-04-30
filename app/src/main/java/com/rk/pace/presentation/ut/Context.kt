package com.rk.pace.presentation.ut

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

fun Context.restartApp() {
    val intent = this.packageManager
        .getLaunchIntentForPackage(this.packageName)

    intent?.addFlags(
        Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
    )
    this.startActivity(intent)
}

fun Context.openAppSettings(): Intent {
    return Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts(
            "package",
            this.packageName,
            null
        )
    )
}