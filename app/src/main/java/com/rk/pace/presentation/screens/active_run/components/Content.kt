package com.rk.pace.presentation.screens.active_run.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.presentation.screens.active_run.ActiveRunViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(
    viewModel: ActiveRunViewModel,
    ready: Boolean = true,
    onNotReadyStartRunClick: () -> Unit = {},
    gpsEnabled: Boolean? = true,
    onGpsDisabledStartRunClick: () -> Unit = {},
    goToSaveRun: () -> Unit,
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
                                when (gpsEnabled) {
                                    true -> viewModel.startRun()
                                    false -> onGpsDisabledStartRunClick()
                                    null -> {}
                                }
                            } else {
                                onNotReadyStartRunClick()
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