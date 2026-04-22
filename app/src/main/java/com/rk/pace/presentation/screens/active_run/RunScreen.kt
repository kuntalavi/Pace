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
import androidx.compose.material3.CircularProgressIndicator
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunScreen(
    viewModel: ActiveRunViewModel,
    goToSaveRun: () -> Unit,
    goBack: () -> Unit
) {

    val context = LocalContext.current
    val activity = context as ComponentActivity
    val lifecycle = LocalLifecycleOwner.current
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    fun evaluate() {
        viewModel.evaluate(
            shouldShowLocationRationale =
                activity.shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
            shouldShowNotificationRationale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                activity.shouldShowRequestPermissionRationale(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            else false
        )
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        val fine = grants[Manifest.permission.ACCESS_FINE_LOCATION] == true

        if (!fine) {
            viewModel.markLocationRequested()
        }

        evaluate()
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            viewModel.markNotificationRequested()
        }
        evaluate()
    }

    val seLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        evaluate()
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            evaluate()
        }
    }

    var showLocationRationaleDialog by remember { mutableStateOf(false) }
    var showNotificationRationaleDialog by remember { mutableStateOf(false) }
    var showGpsRationaleDialog by remember { mutableStateOf(false) }

    when (val state = screenState) {
        is RunScreenState.Load -> {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        colorScheme.surface
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is RunScreenState.Ready -> {
            Content(
                viewModel = viewModel,
                onGpsDisabledStartRunClick = {
                    showGpsRationaleDialog = true
                },
                goToSaveRun = goToSaveRun,
                goBack = goBack
            )
        }

        is RunScreenState.LocationPermissionRequired -> {

            val permissionState = state.state
            when (permissionState) {
                PermissionState.NotRequested -> {
                    LaunchedEffect(key1 = Unit) {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                            )
                        )
                    }
                }

                PermissionState.DeniedOnce -> {
                    Content(
                        viewModel = viewModel,
                        ready = false,
                        onNotReadyStartRunClick = {
                            showLocationRationaleDialog = true
                        },
                        goToSaveRun = goToSaveRun,
                        goBack = goBack
                    )
                }

                PermissionState.DeniedPermanently -> {
                    Content(
                        viewModel = viewModel,
                        ready = false,
                        onNotReadyStartRunClick = {
                            seLauncher.launch(
                                context.openAppSettings()
                            )
                        },
                        goToSaveRun = goToSaveRun,
                        goBack = goBack
                    )
                }

                else -> {}
            }

        }

        is RunScreenState.NotificationPermissionRequired -> {

            val permissionState = state.state
            when (permissionState) {
                PermissionState.NotRequested -> {
                    LaunchedEffect(key1 = Unit) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationPermissionLauncher.launch(
                                Manifest.permission.POST_NOTIFICATIONS
                            )
                        }
                    }
                }

                PermissionState.DeniedOnce -> {
                    Content(
                        viewModel = viewModel,
                        ready = false,
                        onNotReadyStartRunClick = {
                            showNotificationRationaleDialog = true
                        },
                        goToSaveRun = goToSaveRun,
                        goBack = goBack
                    )
                }

                PermissionState.DeniedPermanently -> {
                    Content(
                        viewModel = viewModel,
                        ready = false,
                        onNotReadyStartRunClick = {
                            seLauncher.launch(
                                context.openAppSettings()
                            )
                        },
                        goToSaveRun = goToSaveRun,
                        goBack = goBack
                    )
                }

                else -> {}
            }
        }

        is RunScreenState.GpsDisabledMidRun -> {
            Content(
                viewModel = viewModel,
                goToSaveRun = goToSaveRun,
                goBack = goBack
            )

            AlertDialog(
                onDismissRequest = { },
                icon = {
                    Icon(
                        imageVector = gps_off,
                        contentDescription = null,
                        tint = colorScheme.error
                    )
                },
                title = {
                    Text(
                        text = "GPS Signal Lost"
                    )
                },
                text = {
                    Text(
                        text = "Your run has been automatically paused because GPS was turned off. Re-enable Location Services to continue your run."
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            seLauncher.launch(
                                Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
                                )
                            )
                        }
                    ) {
                        Text("Turn GPS On")
                    }
                },
                dismissButton = null
            )
        }
    }

    if (showGpsRationaleDialog) {
        PermissionRationaleDialog(
            title = "GPS Off",
            text = "Your device's Location Services are disabled. Open GPS Settings to turn it on.",
            confirmLabel = "Turn GPS On",
            dismissLabel = "Fuck Off",
            onConfirm = {
                showGpsRationaleDialog = false
                seLauncher.launch(
                    Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    )
                )
            },
            onDismiss = { showGpsRationaleDialog = false }
        )
    }

    if (showLocationRationaleDialog) {
        PermissionRationaleDialog(
            title = "Precise Location Required",
            text = "Pace needs precise GPS to record your route, distance, and pace accurately.",
            confirmLabel = "Allow",
            dismissLabel = "Not now",
            onConfirm = {
                showLocationRationaleDialog = false
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                    )
                )
            },
            onDismiss = { showLocationRationaleDialog = false }
        )
    }

    if (showNotificationRationaleDialog) {
        PermissionRationaleDialog(
            title = "Stay Updated While You Run",
            text = "Notifications show your live pace and distance in the status bar, even with the screen off..",
            confirmLabel = "Allow",
            dismissLabel = "Skip",
            onConfirm = {
                showNotificationRationaleDialog = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationPermissionLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                } else evaluate()
            },
            onDismiss = {
                showNotificationRationaleDialog = false
                viewModel.skipNotification()
                evaluate()
            }
        )
    }

}