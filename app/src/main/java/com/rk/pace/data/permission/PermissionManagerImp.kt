package com.rk.pace.data.permission

import android.content.Context
import android.os.Build
import com.rk.pace.common.extension.hasPostNotificationPermission
import com.rk.pace.common.extension.hasPreciseForegroundLocationPermission
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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return context.hasPostNotificationPermission()
    }
}