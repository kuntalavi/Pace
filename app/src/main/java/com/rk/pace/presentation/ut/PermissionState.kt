package com.rk.pace.presentation.ut

sealed class PermissionState {
    object NotRequested: PermissionState()
    object Granted : PermissionState()
    object DeniedOnce : PermissionState()
    object DeniedPermanently : PermissionState()
}