package com.rk.pace.presentation.screens.active_run

sealed interface ActiveRunAction {
    object OnStartClick : ActiveRunAction
    object OnPauseClick : ActiveRunAction
    object OnResumeClick : ActiveRunAction
    object OnStopClick : ActiveRunAction
    object OnSaveClick : ActiveRunAction
    data class OnRunTitleChange(
        val title: String
    ) : ActiveRunAction
    object ClearSaveError : ActiveRunAction

    object DismissAllRationale : ActiveRunAction
    object RequestLocationPermission : ActiveRunAction
    object RequestNotificationPermission : ActiveRunAction
    object SkipNotificationPermission : ActiveRunAction

    data class OnLocationPermissionResult(
        val granted: Boolean,
        val shouldShowRationale: Boolean
    ) : ActiveRunAction

    data class OnNotificationPermissionResult(
        val granted: Boolean,
        val shouldShowRationale: Boolean
    ) : ActiveRunAction

    object CheckNotReadyWarn: ActiveRunAction

}