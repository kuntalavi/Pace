package com.rk.pace.presentation.screens.active_run

data class ActiveRunUiState(

    val warning: NotReadyWarn? = null,

    val openSystemLocationPrompt: Boolean = false,

    val showLocationPermissionRationale: Boolean = false,
    val showLocationPermissionSttRationale: Boolean = false,
    val showNotificationPermissionRationale: Boolean = false,
    val showNotificationPermissionSttRationale: Boolean = false,
    val showGpsOffRationale: Boolean = false,

    val showGpsInterruptedRationale: Boolean = false,

    val runTitle: String = "",
    val saving: Boolean = false,
    val isRunSaved: Boolean = false,
    val saveError: String? = null
)