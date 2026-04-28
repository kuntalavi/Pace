package com.rk.pace.presentation.screens.active_run

sealed interface ActiveRunAction {
    object OnStartRunClick : ActiveRunAction
    object OnPauseRunClick : ActiveRunAction
    object OnResumeRunClick : ActiveRunAction
    object OnStopRunClick : ActiveRunAction
    object OnSaveRunClick : ActiveRunAction
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

    object InitialLocationPromptFired: ActiveRunAction

    object CheckNotReadyWarn: ActiveRunAction

}