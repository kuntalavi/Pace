package com.rk.pace.data.permission

import android.content.Context
import androidx.core.content.edit
import com.rk.pace.domain.permission.UserPref
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserPrefImp @Inject constructor(
    @param:ApplicationContext private val context: Context
) : UserPref {

    private val prefs = context.getSharedPreferences(
        "pace_prefs",
        Context.MODE_PRIVATE
    )

    override fun markLocationPermissionRequested() {
        prefs.edit {
            putBoolean(
                "location_ever_requested",
                true
            )
        }
    }

    override fun wasLocationPermissionRequested(): Boolean {
        return prefs.getBoolean(
            "location_ever_requested",
            false
        )
    }

    override fun markNotificationPermissionRequested() {
        prefs.edit {
            putBoolean(
                "notification_ever_requested",
                true
            )
        }
    }

    override fun wasNotificationPermissionRequested(): Boolean {
        return prefs.getBoolean(
            "notification_ever_requested",
            false
        )
    }
}