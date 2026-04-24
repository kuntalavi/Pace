package com.rk.pace.domain.permission

interface PermissionManager {

    fun hasPreciseLocationPermission(): Boolean
    fun hasNotificationPermission(): Boolean

}