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
    onNotReadyStartRunClick: () -> Unit = {},
    onGpsDisabledStartRunClick: () -> Unit = {},
    goToSaveRun: () -> Unit,
    goBack: () -> Unit
) {

    val runState by viewModel.runState.collectAsStateWithLifecycle()
    val location by viewModel.location.collectAsStateWithLifecycle()
    val gpsStrength by viewModel.gpsStrength.collectAsStateWithLifecycle()
    val gpsEnabled by viewModel.gpsEnabled.collectAsStateWithLifecycle()

    var mapLoaded by rememberSaveable { mutableStateOf(false) }

    ActiveRunContent(
        runState = runState,
        location = location,
        gpsStrength = gpsStrength,
        mapLoaded = mapLoaded,
        onMapLoaded = { mapLoaded = true },
        onStartClick = {
            if (ready) {
                when (gpsEnabled) {
                    true -> viewModel.startRun()
                    false -> onGpsDisabledStartRunClick()
                    null -> {}
                }
            } else {
                onNotReadyStartRunClick()
            }
        },
        onPauseClick = {
            viewModel.pauseRun()
        },
        onResumeClick = {
            viewModel.resumeRun()
        },
        onStopClick = {
            viewModel.pauseRun()
            goToSaveRun()
        },
        onBackClick = goBack
    )


}