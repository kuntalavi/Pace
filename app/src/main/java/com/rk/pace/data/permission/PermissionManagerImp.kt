package com.rk.pace.data.permission

import android.content.Context
import com.rk.pace.data.ut.hasPostNotificationPermission
import com.rk.pace.data.ut.hasPreciseForegroundLocationPermission
import com.rk.pace.domain.permission.PermissionManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PermissionManagerImp @Inject constructor(
    @param:ApplicationContext private val context: Context
) : PermissionManager {
    override fun hasPreciseLocationPermission(): Boolean {
        return context.hasPreciseForegroundLocationPermission()
    }

    override fun hasNotificationPermission(): Boolean {
        return context.hasPostNotificationPermission()
    }
}