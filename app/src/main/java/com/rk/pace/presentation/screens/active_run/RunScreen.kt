package com.rk.pace.presentation.screens.active_run

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.model.RunState
import com.rk.pace.domain.tracking.GpsStrength
import com.rk.pace.presentation.screens.active_run.components.Content
import com.rk.pace.presentation.ut.PermissionRationaleDialog

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
    onCompleteRun: () -> Unit,
    onBackClick: () -> Unit,
) {

    Content(
        warning = state.warning?.message,
        runState = runState,
        location = location,
        gpsStrength = gpsStrength,
        onStartRun = { onAction(ActiveRunAction.OnStartClick) },
        onResumeRun = { onAction(ActiveRunAction.OnResumeClick) },
        onPauseRun = { onAction(ActiveRunAction.OnPauseClick) },
        onStopRun = {
            onAction(ActiveRunAction.OnPauseClick)
            onCompleteRun()
        },
        onBackClick = onBackClick
    )

    when {
        state.showLocationPermissionRationale -> {
            PermissionRationaleDialog(
                title = "Precise Location Required",
                text = "Pace needs Precise Location to record your route and pace accurately.",
                confirmLabel = "Allow",
                dismissLabel = "Not Now",
                onConfirm = onLaunchLocationPermission,
                onDismiss = { onAction(ActiveRunAction.DismissAllRationale) }
            )
        }

        state.showLocationPermissionSttRationale -> {
            PermissionRationaleDialog(
                title = "Enable Precise Location",
                text = "Location access is disabled. Open app settings and allow Location with Precise Location turned on.",
                confirmLabel = "Open Settings",
                dismissLabel = "Not now",
                onConfirm = onOpenAppSett,
                onDismiss = { onAction(ActiveRunAction.DismissAllRationale) }
            )
        }

        state.showGpsOffRationale -> {
            PermissionRationaleDialog(
                title = "Location Services Off",
                text = "Turn on device Location Services before starting so Pace can record an accurate GPS route.",
                confirmLabel = "Turn On",
                dismissLabel = "Not Now",
                onConfirm = onOpenLocationSett,
                onDismiss = { onAction(ActiveRunAction.DismissAllRationale) }
            )
        }

        state.showNotificationPermissionRationale -> {
            PermissionRationaleDialog(
                title = "Stay Updated While You Run",
                text = "Notifications show your live run progress while Pace is tracking in the background.",
                confirmLabel = "Allow",
                dismissLabel = "Skip",
                onConfirm = onLaunchNotificationPermission,
                onDismiss = { onAction(ActiveRunAction.SkipNotificationPermission) }
            )
        }

        state.showNotificationPermissionSttRationale -> {
            PermissionRationaleDialog(
                title = "Notifications Disabled",
                text = "You can still track this run. Enable notifications in Settings if you want live progress while the screen is off.",
                confirmLabel = "Open Settings",
                dismissLabel = "Continue",
                onConfirm = onOpenAppSett,
                onDismiss = { onAction(ActiveRunAction.SkipNotificationPermission) }
            )
        }

        state.showGpsInterruptedRationale -> {
            AlertDialog(
                onDismissRequest = { onAction(ActiveRunAction.DismissAllRationale) },
                icon = {},
                title = { Text("GPS Turned Off") },
                text = { Text("Pace paused your run because device Location Services were turned off. Turn them back on before resuming.") },
                confirmButton = {
                    TextButton(onClick = { onOpenLocationSett() }) { Text("Open Settings") }
                },
                dismissButton = {
                    TextButton(onClick = { onAction(ActiveRunAction.DismissAllRationale) }) { Text("Stay Paused") }
                }
            )
        }
    }

}