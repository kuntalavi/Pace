package com.rk.pace.domain.permission

interface UserPref {

    fun markLocationPermissionRequested()
    fun markNotificationPermissionRequested()

    fun wasLocationPermissionRequested(): Boolean
    fun wasNotificationPermissionRequested(): Boolean

}