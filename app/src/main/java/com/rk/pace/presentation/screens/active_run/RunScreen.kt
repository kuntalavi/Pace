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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.rk.pace.R
import com.rk.pace.presentation.ut.PermissionRationaleDialog
import com.rk.pace.presentation.screens.active_run.components.RunBottomSheet
import com.rk.pace.presentation.screens.active_run.components.RunMap
import com.rk.pace.presentation.ut.PermissionState

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
    val gpsEnabled by viewModel.gpsEnabled.collectAsStateWithLifecycle()

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

    val locationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        evaluate()
    }

    val notificationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) viewModel.skipNotification()
        evaluate()
    }

    val settingsLauncher = rememberLauncherForActivityResult(
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
    var showGpsPreRunDialog by remember { mutableStateOf(false) }

    when (val state = screenState) {
        is RunScreenState.Load -> {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is RunScreenState.Ready -> {
            Content(
                viewModel = viewModel,
                goToSaveRun = goToSaveRun,
                gpsEnabled = gpsEnabled,
                gpsDisabledStart = {
                    showGpsPreRunDialog = true
                }
            )
        }

        is RunScreenState.LocationPermissionRequired -> {

            val permissionState = state.state
            when (permissionState) {
                PermissionState.NotRequested -> {
                    viewModel.markLocationRequested()
                    locationLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                        )
                    )
                }

                PermissionState.DeniedOnce -> {
                    Content(
                        ready = false,
                        onStartRunClick = {
                            showLocationRationaleDialog = true
                        },
                        viewModel = viewModel,
                        goToSaveRun = goToSaveRun
                    )
                }

                PermissionState.DeniedPermanently -> {
                    Content(
                        ready = false,
                        onStartRunClick = {
                            settingsLauncher.launch(
                                appSettingsIntent(context)
                            )
                        },
                        viewModel = viewModel,
                        goToSaveRun = goToSaveRun
                    )
                }

                else -> {}
            }

        }

        is RunScreenState.NotificationPermissionRequired -> {

            val permissionState = state.state
            when (permissionState) {
                PermissionState.NotRequested -> {
                    viewModel.markNotificationRequested()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notificationLauncher.launch(
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    }
                }

                PermissionState.DeniedOnce -> {
                    Content(
                        ready = false,
                        onStartRunClick = {
                            showNotificationRationaleDialog = true
                        },
                        viewModel = viewModel,
                        goToSaveRun = goToSaveRun
                    )
                }

                PermissionState.DeniedPermanently -> {
                    Content(
                        ready = false,
                        onStartRunClick = {
                            settingsLauncher.launch(
                                appSettingsIntent(context)
                            )
                        },
                        viewModel = viewModel,
                        goToSaveRun = goToSaveRun
                    )
                }

                else -> {}
            }
        }

        is RunScreenState.GpsDisabledMidRun -> {
            Content(
                viewModel = viewModel,
                goToSaveRun = goToSaveRun
            )

            AlertDialog(
                onDismissRequest = { },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.gps_off_24px),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
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
                            settingsLauncher.launch(
                                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
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

    if (showGpsPreRunDialog) {
        PermissionRationaleDialog(
            title = "GPS is Off",
            text = "Your device's Location Services are disabled. Open GPS Settings to turn it on.",
            confirmLabel = "Turn GPS On",
            dismissLabel = "Cancel",
            onConfirm = {
                showGpsPreRunDialog = false
                settingsLauncher.launch(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                )
            },
            onDismiss = { showGpsPreRunDialog = false }
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
                viewModel.markLocationRequested()
                locationLauncher.launch(
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
                viewModel.markNotificationRequested()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    viewModel: ActiveRunViewModel,
    ready: Boolean = true,
    onStartRunClick: () -> Unit = {},
    goToSaveRun: () -> Unit,
    gpsEnabled: Boolean = true,
    gpsDisabledStart: () -> Unit = {}
) {

    val runState by viewModel.runState.collectAsStateWithLifecycle()
    val location by viewModel.location.collectAsStateWithLifecycle()
    val gpsStrength by viewModel.gpsStrength.collectAsStateWithLifecycle()

    var isMapLoaded by remember { mutableStateOf(false) }

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true
        )
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(
                            MaterialTheme.colorScheme.background
                        )
                        .padding(horizontal = 20.dp)
                ) {
                    RunBottomSheet(
                        gpsStrength = gpsStrength,
                        runState = runState,
                        isMapLoaded = isMapLoaded,
                        start = {
                            if (ready) {
                                if (gpsEnabled) {
                                    viewModel.startRun()
                                } else {
                                    gpsDisabledStart()
                                }
                            } else {
                                onStartRunClick()
                            }
                        },
                        pause = {
                            viewModel.pauseRun()
                        },
                        resume = {
                            viewModel.resumeRun()
                        },
                        stop = {
                            viewModel.pauseRun()
                            goToSaveRun()
                        }
                    )
                }
            },
            sheetDragHandle = null,
            sheetSwipeEnabled = false,
            sheetShape = RoundedCornerShape(0.dp),
            sheetPeekHeight = 300.dp,
            sheetMaxWidth = Dp.Unspecified
        ) {
            RunMap(
                modifier = Modifier
                    .fillMaxSize(),
                segments = runState.segments,
                currentLocation = location,
                isAct = runState.isAct,
                paused = runState.paused,
                bottomPaddingDp = 300.dp,
                onMapLoadedCallback = {
                    isMapLoaded = true
                }
            )
        }

        if (!isMapLoaded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.background
                    )
                    .zIndex(2f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

private fun appSettingsIntent(context: android.content.Context) =
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = android.net.Uri.fromParts("package", context.packageName, null)
    }