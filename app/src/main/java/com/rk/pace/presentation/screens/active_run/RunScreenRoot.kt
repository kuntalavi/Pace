package com.rk.pace.presentation.screens.active_run

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.rk.pace.common.extension.openAppSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunScreenRoot(
    viewModel: ActiveRunViewModel,
    onFinishRunClick: () -> Unit,
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val activity = LocalActivity.current as? ComponentActivity ?: return
    val lifecycleOwner = LocalLifecycleOwner.current

    val state by viewModel.state.collectAsStateWithLifecycle()
    val runState by viewModel.runState.collectAsStateWithLifecycle()
    val location by viewModel.location.collectAsStateWithLifecycle()
    val gpsStrength by viewModel.gpsStrength.collectAsStateWithLifecycle()

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(
            Lifecycle.State.RESUMED
        ) {
            viewModel.onAction(
                ActiveRunAction.CheckNotReadyWarn
            )
        }
    }

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

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = result[Manifest.permission.ACCESS_FINE_LOCATION] != false
        viewModel.onAction(
            ActiveRunAction.OnLocationPermissionResult(
                granted,
                shouldShowLocationRationale()
            )
        )
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        viewModel.onAction(
            ActiveRunAction.OnNotificationPermissionResult(
                result,
                shouldShowNotificationRationale()
            )
        )
    }

    val sttLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.onAction(
            ActiveRunAction.DismissAllRationale
        )
    }

    LaunchedEffect(key1 = state.openSystemLocationPrompt) {
        if (state.openSystemLocationPrompt) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            viewModel.onAction(ActiveRunAction.InitialLocationPromptFired)
        }
    }

    RunScreen(
        state = state,
        runState = runState,
        location = location,
        gpsStrength = gpsStrength,
        onAction = viewModel::onAction,
        onLaunchLocationPermission = {
            viewModel.onAction(
                ActiveRunAction.RequestLocationPermission
            )
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        },
        onLaunchNotificationPermission = {
            viewModel.onAction(
                ActiveRunAction.RequestNotificationPermission
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        },
        onOpenAppSett = {
            sttLauncher.launch(
                context.openAppSettings()
            )
        },
        onOpenLocationSett = {
            sttLauncher.launch(
                Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
                )
            )
        },
        onStopRunClick = onFinishRunClick,
        onBack = onBack
    )

}
