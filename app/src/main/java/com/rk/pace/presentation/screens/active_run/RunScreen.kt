package com.rk.pace.presentation.screens.active_run

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.common.extension.hasLocationPermission
import com.rk.pace.presentation.screens.active_run.components.RunMap
import com.rk.pace.presentation.screens.active_run.components.StatsBottomSheet
import com.rk.pace.theme.White

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunScreen(
    viewModel: ActiveRunViewModel,
    goToSaveRun: () -> Unit,
    goBack: () -> Unit
) {

    val context = LocalContext.current

    val runState by viewModel.runState.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val requestLocationPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            goBack()
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (!context.hasLocationPermission()) {
            requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    val sheetState = rememberStandardBottomSheetState()
    val scaffoldState = rememberBottomSheetScaffoldState(sheetState)


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                StatsBottomSheet(
                    runState = runState,
                    start = { viewModel.startRun() },
                    pause = { viewModel.pauseRun() },
                    resume = { viewModel.resumeRun() },
                    stop = {
                        goToSaveRun()
                    }
                )
            },
            sheetDragHandle = null,
            sheetSwipeEnabled = false,
            sheetShape = BottomSheetDefaults.HiddenShape,
            sheetPeekHeight = 200.dp
        ) {
            RunMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                segments = runState.segments,
                onMapLoaded = { viewModel.onMapLoaded() },
                isMapLoaded = state.isMapLoaded
//            captureBitmap = state.captureBitmap,
//            onBitmapReady = { bitmap ->
//                viewModel.onBitmapReady(bitmap)
//            }
            )
        }
        if (!state.isMapLoaded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        White
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}