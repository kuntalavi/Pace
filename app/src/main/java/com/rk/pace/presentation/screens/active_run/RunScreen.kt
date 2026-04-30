package com.rk.pace.presentation.screens.active_run

import androidx.compose.runtime.Composable
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.model.RunState
import com.rk.pace.domain.tracker.GpsStrength
import com.rk.pace.presentation.screens.active_run.components.Content
import com.rk.pace.presentation.components.PermissionRationaleDialog

@Composable
fun RunScreen(
    state: ActiveRunUiState,
    runState: RunState,
    location: RunPathPoint?,
    gpsStrength: GpsStrength,
    onAction: (ActiveRunAction) -> Unit,
    onLaunchLocationPermission: () -> Unit,
    onLaunchNotificationPermission: () -> Unit,
    onOpenAppSett: () -> Unit,
    onOpenLocationSett: () -> Unit,
    onStopRunClick: () -> Unit,
    onBack: () -> Unit,
) {

    Content(
        warning = state.warning?.message,
        runState = runState,
        location = location,
        gpsStrength = gpsStrength,
        onStartRun = { onAction(ActiveRunAction.OnStartRunClick) },
        onResumeRun = { onAction(ActiveRunAction.OnResumeRunClick) },
        onPauseRun = { onAction(ActiveRunAction.OnPauseRunClick) },
        onStopRun = {
            onAction(ActiveRunAction.OnPauseRunClick)
            onStopRunClick()
        },
        onBack = onBack
    )

    when {
        state.showLocationPermissionRationale -> {
            PermissionRationaleDialog(
                title = "Precise Location Required",
                body = "Pace needs Precise Location to record your route and pace accurately.",
                confirmLabel = "Allow",
                dismissLabel = "Not Now",
                onConfirm = onLaunchLocationPermission,
                onDismiss = { onAction(ActiveRunAction.DismissAllRationale) }
            )
        }

        state.showLocationPermissionSttRationale -> {
            PermissionRationaleDialog(
                title = "Enable Precise Location",
                body = "Location access is disabled. Open app settings and allow Location with Precise Location turned on.",
                confirmLabel = "Okay",
                dismissLabel = "Not Now",
                onConfirm = onOpenAppSett,
                onDismiss = { onAction(ActiveRunAction.DismissAllRationale) }
            )
        }

        state.showGpsOffRationale -> {
            PermissionRationaleDialog(
                title = "Location Services Off",
                body = "Turn on device Location Services before starting so Pace can record an accurate GPS route.",
                confirmLabel = "Turn On",
                dismissLabel = "Not Now",
                onConfirm = onOpenLocationSett,
                onDismiss = { onAction(ActiveRunAction.DismissAllRationale) }
            )
        }

        state.showNotificationPermissionRationale -> {
            PermissionRationaleDialog(
                title = "Stay Updated While You Run",
                body = "Notifications show your live run progress while Pace is tracking in the background.",
                confirmLabel = "Allow",
                dismissLabel = "Skip",
                onConfirm = onLaunchNotificationPermission,
                onDismiss = { onAction(ActiveRunAction.SkipNotificationPermission) }
            )
        }

        state.showNotificationPermissionSttRationale -> {
            PermissionRationaleDialog(
                title = "Notifications Disabled",
                body = "You can still track this run. Enable notifications in Settings if you want live progress while the screen is off.",
                confirmLabel = "Okay",
                dismissLabel = "Skip",
                onConfirm = onOpenAppSett,
                onDismiss = { onAction(ActiveRunAction.SkipNotificationPermission) }
            )
        }

        state.showGpsInterruptedRationale -> {
            PermissionRationaleDialog(
                title = "GPS Turned Off",
                body = "Pace paused your run because device Location Services were turned off. Turn them back on before resuming.",
                confirmLabel = "Okay",
                dismissLabel = "Stay Paused",
                onConfirm = onOpenLocationSett,
                onDismiss = { onAction(ActiveRunAction.DismissAllRationale) }
            )
        }
    }

}