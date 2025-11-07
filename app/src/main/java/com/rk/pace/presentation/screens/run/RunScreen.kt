package com.rk.pace.presentation.screens.run

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.common.extension.hasLocationPermission
import com.rk.pace.presentation.screens.run.components.RunMap
import com.rk.pace.presentation.screens.run.components.RunTopB
import com.rk.pace.presentation.theme.Black

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunScreen(
    viewModel: RunViewModel = hiltViewModel(),
    goBack: () -> Unit
) {

    val context = LocalContext.current

    val runState by viewModel.runState.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val requestLocationPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) viewModel.setHasLocationPermission()
    }

    LaunchedEffect(runState) {
        Log.d(
            "runstate",
            "time: ${runState.durationInM} distance: ${runState.distanceInMeters} isAct: ${runState.isAct}"
        )
    }

    LaunchedEffect(Unit) {
        if (context.hasLocationPermission()) {
            viewModel.setHasLocationPermission()
        } else {
            requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RunTopB(
                goBack = goBack
            )
        }
    ) { it ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                ShowMapLoadingProgressIndicator(!state.isMapLoaded)
                RunMap(
                    hasLocationPermission = state.hasLocationPermission,
                    segments = runState.segments,
                    onMapLoaded = { viewModel.onMapLoaded() }
                )
            }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                LargeFloatingActionButton(
                    onClick = {
                        if (runState.isAct) {
                            if (runState.paused) {
                                viewModel.resumeRun()
                            } else {
                                viewModel.pauseRun()
                            }
                        } else {
                            viewModel.startRun()
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    if (runState.isAct) {
                        if (runState.paused) {
                            Text(text = "Resume", color = Black)
                        } else {
                            Text(text = "Pause", color = Black)
                        }
                    } else {
                        Text("Start", color = Black)
                    }
                }
                }
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
                if (runState.isAct) {
                    LargeFloatingActionButton(
                        onClick = {
                            viewModel.stopRun()
                            Log.d("pathPoints", "Run Path Points ${runState.segments}")
                        },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Text(text = "Stop", color = Black)
                    }
                }
            }

        }

    }

@Composable
private fun ShowMapLoadingProgressIndicator(
    visible: Boolean = false
) {
    AnimatedVisibility(
        modifier = Modifier
            .fillMaxSize(),
        visible = visible,
        enter = EnterTransition.None,
        exit = fadeOut(),
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .wrapContentSize()
        )
    }
}