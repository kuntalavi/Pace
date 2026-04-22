package com.rk.pace.presentation.screens.active_run

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.rk.pace.common.extension.openAppSettings
import com.rk.pace.presentation.screens.active_run.components.Content
import com.rk.pace.presentation.ut.PermissionRationaleDialog
import com.rk.pace.presentation.ut.PermissionState
import com.rk.pace.theme.gps_off

private enum class TrackAction {
    START,
    RESUME
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunScreen(
    viewModel: ActiveRunViewModel,
    goToSaveRun: () -> Unit,
    goBack: () -> Unit
) {

    val context = LocalContext.current
    val activity = context as ComponentActivity
    val lifecycleOwner = LocalLifecycleOwner.current
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    var action by remember { mutableStateOf<TrackAction?>(null) }
    var showLocationPermissionRationale by remember { mutableStateOf(false) }
    var showLocationPermissionSttRationale by remember { mutableStateOf(false) }
    var showLocationPermissionInterruptedRationale by remember { mutableStateOf(false) }
    var showNotificationPermissionRationale by remember { mutableStateOf(false) }
    var showNotificationPermissionSttRationale by remember { mutableStateOf(false) }
    var showGpsOffRationale by remember { mutableStateOf(false) }
    var showGpsInterruptedRationale by remember { mutableStateOf(false) }

    fun shouldShowLocationRationale() =
        activity.shouldShowRequestPermissionRationale(
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    fun shouldShowNotificationRationale() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.shouldShowRequestPermissionRationale(
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else false

    fun evaluate() {
        viewModel.evaluate(
            shouldShowLocationRationale = shouldShowLocationRationale()
        )
    }

    fun rTrackAction(action: TrackAction) {
        when (action) {
            TrackAction.START -> viewModel.startRun()
            TrackAction.RESUME -> viewModel.resumeRun()
        }
    }

    var continueTrackAction: () -> Unit = {}

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        evaluate()

        val fineGranted = grants[Manifest.permission.ACCESS_FINE_LOCATION] != false
        if (fineGranted) {
            continueTrackAction()
        } else {
            action = null
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        evaluate()
        continueTrackAction()
    }

    val sttLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        evaluate()
    }

    fun requestLocationPermission() {
        viewModel.markLocationRequested()
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
    }

    fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            viewModel.markNotificationRequested()
            notificationPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            viewModel.skipNotification()
            continueTrackAction()
        }
    }

    fun handleLocationRequirement(
        permissionState: PermissionState,
        a: TrackAction? = null
    ) {
        action = a

        when (permissionState) {
            PermissionState.NotRequested -> requestLocationPermission()
            PermissionState.DeniedOnce -> showLocationPermissionRationale = true
            PermissionState.DeniedPermanently -> showLocationPermissionSttRationale = true
            PermissionState.Granted -> Unit
        }
    }

    fun handleNotificationRequirement(
        permissionState: PermissionState,
        a: TrackAction
    ) {
        action = a

        when (permissionState) {
            PermissionState.NotRequested -> requestNotificationPermission()
            PermissionState.DeniedOnce -> showNotificationPermissionRationale = true
            PermissionState.DeniedPermanently -> showNotificationPermissionSttRationale = true
            PermissionState.Granted -> Unit
        }
    }

    fun prepareTrackAction(a: TrackAction) {
        val locationState = viewModel.locationPermissionState(
            shouldShowLocationRationale()
        )
        if (locationState != PermissionState.Granted) {
            evaluate()
            handleLocationRequirement(
                locationState,
                a
            )
            return
        }

        if (viewModel.gpsEnabled.value == false) {
            action = null
            showGpsOffRationale = true
            return
        }

        val notificationState = viewModel.notificationPermissionState(
            shouldShowNotificationRationale()
        )
        if (notificationState != PermissionState.Granted) {
            handleNotificationRequirement(
                notificationState,
                a
            )
            return
        }

        action = null
        rTrackAction(a)
    }

    continueTrackAction = action@{
        val action = action ?: return@action

        if (
            viewModel.locationPermissionState(
                shouldShowLocationRationale()
            ) != PermissionState.Granted
        ) {
            evaluate()
            return@action
        }

        prepareTrackAction(action)
    }

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            evaluate()
        }
    }

    when (val state = screenState) {
        is RunScreenState.Load -> {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        colorScheme.surface
                    ),
                contentAlignment = Alignment.Center
            ) { }
        }

        is RunScreenState.Ready -> {
            Content(
                viewModel = viewModel,
                onStartRun = { prepareTrackAction(TrackAction.START) },
                onResumeRun = { prepareTrackAction(TrackAction.RESUME) },
                goToSaveRun = goToSaveRun,
                goBack = goBack
            )
        }

        is RunScreenState.LocationPermissionRequired -> {
            LaunchedEffect(state) {
                if (state.mRun) {
                    showLocationPermissionInterruptedRationale = true
                }
            }

            Content(
                viewModel = viewModel,
                ready = false,
                notReadyActionText = if (state.state == PermissionState.DeniedPermanently) {
                    "Open Settings"
                } else {
                    "Enable Location"
                },
                onNotReadyTrackingClick = {
                    handleLocationRequirement(state.state)
                },
                onStartRun = { prepareTrackAction(TrackAction.START) },
                onResumeRun = { prepareTrackAction(TrackAction.RESUME) },
                goToSaveRun = goToSaveRun,
                goBack = goBack
            )
        }

        is RunScreenState.NotificationPermissionRequired -> {
            Content(
                viewModel = viewModel,
                onStartRun = { prepareTrackAction(TrackAction.START) },
                onResumeRun = { prepareTrackAction(TrackAction.RESUME) },
                goToSaveRun = goToSaveRun,
                goBack = goBack
            )
        }

        is RunScreenState.GpsDisabledMRun -> {
            LaunchedEffect(Unit) {
                showGpsInterruptedRationale = true
            }

            Content(
                viewModel = viewModel,
                onStartRun = { prepareTrackAction(TrackAction.START) },
                onResumeRun = { prepareTrackAction(TrackAction.RESUME) },
                goToSaveRun = goToSaveRun,
                goBack = goBack
            )
        }
    }

    if (showGpsOffRationale) {
        PermissionRationaleDialog(
            title = "Location Services Off",
            text = "Turn on device Location Services before starting so Pace can record an accurate GPS route.",
            confirmLabel = "Open Settings",
            dismissLabel = "Not now",
            onConfirm = {
                showGpsOffRationale = false
                sttLauncher.launch(
                    Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    )
                )
            },
            onDismiss = {
                showGpsOffRationale = false
                action = null
            }
        )
    }

    if (showLocationPermissionRationale) {
        PermissionRationaleDialog(
            title = "Precise Location Required",
            text = "Pace needs Precise Location to record your route, distance, and pace accurately.",
            confirmLabel = "Allow",
            dismissLabel = "Not now",
            onConfirm = {
                showLocationPermissionRationale = false
                requestLocationPermission()
            },
            onDismiss = {
                showLocationPermissionRationale = false
                action = null
            }
        )
    }

    if (showLocationPermissionSttRationale) {
        PermissionRationaleDialog(
            title = "Enable Precise Location",
            text = "Location access is disabled for Pace. Open app settings and allow Location with Precise Location turned on.",
            confirmLabel = "Open Settings",
            dismissLabel = "Not now",
            onConfirm = {
                showLocationPermissionSttRationale = false
                action = null
                sttLauncher.launch(
                    context.openAppSettings()
                )
            },
            onDismiss = {
                showLocationPermissionSttRationale = false
                action = null
            }
        )
    }

    if (showNotificationPermissionRationale) {
        PermissionRationaleDialog(
            title = "Stay Updated While You Run",
            text = "Notifications show your live run progress while Pace is tracking in the background.",
            confirmLabel = "Allow",
            dismissLabel = "Continue",
            onConfirm = {
                showNotificationPermissionRationale = false
                requestNotificationPermission()
            },
            onDismiss = {
                showNotificationPermissionRationale = false
                viewModel.skipNotification()
                continueTrackAction()
            }
        )
    }

    if (showNotificationPermissionSttRationale) {
        PermissionRationaleDialog(
            title = "Notifications Disabled",
            text = "You can still track this run. Enable notifications in Settings if you want live progress while the screen is off.",
            confirmLabel = "Open Settings",
            dismissLabel = "Continue",
            onConfirm = {
                showNotificationPermissionSttRationale = false
                action = null
                viewModel.skipNotification()
                sttLauncher.launch(
                    context.openAppSettings()
                )
            },
            onDismiss = {
                showLocationPermissionSttRationale = false
                viewModel.skipNotification()
                continueTrackAction()
            }
        )
    }

    if (
        showGpsInterruptedRationale &&
        screenState == RunScreenState.GpsDisabledMRun
    ) {
        AlertDialog(
            onDismissRequest = {
                showGpsInterruptedRationale = false
            },
            icon = {
                Icon(
                    imageVector = gps_off,
                    contentDescription = null,
                    tint = colorScheme.error
                )
            },
            title = {
                Text(
                    text = "GPS Turned Off"
                )
            },
            text = {
                Text(
                    text = "Pace paused your run because device Location Services were turned off. Turn them back on before resuming."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showGpsInterruptedRationale = false
                        sttLauncher.launch(
                            Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS
                            )
                        )
                    }
                ) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showGpsInterruptedRationale = false
                    }
                ) {
                    Text("Stay Paused")
                }
            }
        )
    }

    val locationIssueState = screenState as? RunScreenState.LocationPermissionRequired
    if (
        showLocationPermissionInterruptedRationale &&
        locationIssueState?.mRun == true
    ) {
        AlertDialog(
            onDismissRequest = {
                showLocationPermissionInterruptedRationale = false
            },
            icon = {
                Icon(
                    imageVector = com.rk.pace.theme.location,
                    contentDescription = null,
                    tint = colorScheme.error
                )
            },
            title = {
                Text(
                    text = "Location Access Lost"
                )
            },
            text = {
                Text(
                    text = "Pace paused your run because Precise Location is unavailable. Restore it before resuming, or stop and save the run so far."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLocationPermissionInterruptedRationale = false
                        when (locationIssueState.state) {
                            PermissionState.DeniedPermanently -> {
                                sttLauncher.launch(
                                    context.openAppSettings()
                                )
                            }

                            PermissionState.NotRequested,
                            PermissionState.DeniedOnce -> requestLocationPermission()

                            PermissionState.Granted -> Unit
                        }
                    }
                ) {
                    Text(
                        if (locationIssueState.state == PermissionState.DeniedPermanently) {
                            "Open Settings"
                        } else {
                            "Allow Location"
                        }
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showLocationPermissionInterruptedRationale = false
                    }
                ) {
                    Text("Stay Paused")
                }
            }
        )
    }
}
