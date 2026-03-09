package com.rk.pace.presentation.screens.active_run

import com.rk.pace.presentation.ut.PermissionState

sealed class RunScreenState {
    object Load: RunScreenState()
    object Ready: RunScreenState()
    data class LocationPermissionRequired(val state: PermissionState): RunScreenState()
    data class NotificationPermissionRequired(val state: PermissionState): RunScreenState()
    object GpsDisabledMidRun: RunScreenState()
}