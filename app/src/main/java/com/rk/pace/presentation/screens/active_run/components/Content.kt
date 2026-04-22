package com.rk.pace.presentation.screens.active_run.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.presentation.screens.active_run.ActiveRunViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(
    viewModel: ActiveRunViewModel,
    ready: Boolean = true,
    notReadyActionText: String = "Enable Location",
    onNotReadyTrackingClick: () -> Unit = {},
    onStartRun: () -> Unit = viewModel::startRun,
    onResumeRun: () -> Unit = viewModel::resumeRun,
    goToSaveRun: () -> Unit,
    goBack: () -> Unit
) {

    val runState by viewModel.runState.collectAsStateWithLifecycle()
    val location by viewModel.location.collectAsStateWithLifecycle()
    val gpsStrength by viewModel.gpsStrength.collectAsStateWithLifecycle()
    val gpsEnabled by viewModel.gpsEnabled.collectAsStateWithLifecycle()

    var mapLoaded by rememberSaveable { mutableStateOf(false) }
    val startText = when {
        !ready -> notReadyActionText
        gpsEnabled == false -> "Turn GPS On"
        else -> "Start Run"
    }
    val resumeText = when {
        !ready -> notReadyActionText.uppercase()
        gpsEnabled == false -> "TURN GPS ON"
        else -> "RESUME"
    }

    ActiveRunContent(
        runState = runState,
        location = location,
        gpsStrength = gpsStrength,
        mapLoaded = mapLoaded,
        onMapLoaded = { mapLoaded = true },
        onStartClick = {
            if (ready) {
                onStartRun()
            } else {
                onNotReadyTrackingClick()
            }
        },
        onPauseClick = {
            viewModel.pauseRun()
        },
        onResumeClick = {
            if (ready) {
                onResumeRun()
            } else {
                onNotReadyTrackingClick()
            }
        },
        onStopClick = {
            viewModel.pauseRun()
            goToSaveRun()
        },
        onBackClick = goBack,
        startText = startText,
        resumeText = resumeText
    )


}
