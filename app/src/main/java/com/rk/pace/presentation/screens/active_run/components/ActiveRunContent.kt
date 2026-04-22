package com.rk.pace.presentation.screens.active_run.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.rk.pace.common.Constants.shape
import com.rk.pace.common.extension.hasPreciseForegroundLocationPermission
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.model.RunState
import com.rk.pace.domain.tracking.GpsStrength
import com.rk.pace.theme.arrowLeft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveRunContent(
    runState: RunState,
    location: RunPathPoint,
    gpsStrength: GpsStrength,
    mapLoaded: Boolean,
    onMapLoaded: () -> Unit,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onStopClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true
        )
    )
    val context = LocalContext.current
    var moveToUserTrigger by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContainerColor = colorScheme.surface,
            containerColor = colorScheme.surface,
            sheetContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorScheme.surface)
                        .padding(
                            horizontal = 20.dp
                        )
                ) {
                    RunBottomSheet(
                        runState = runState,
                        isMapLoaded = mapLoaded,
                        start = onStartClick,
                        pause = onPauseClick,
                        resume = onResumeClick,
                        stop = onStopClick
                    )
                }
            },
            sheetShape = RectangleShape,
            sheetPeekHeight = 300.dp,
            sheetMaxWidth = Dp.Unspecified
        ) { p ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(p)
            ) {
                RunMap(
                    modifier = Modifier.fillMaxSize(),
                    segments = runState.segments,
                    currentLocation = location,
                    moveToUserTrigger = moveToUserTrigger,
                    isAct = runState.isAct,
                    paused = runState.paused,
                    bottomPaddingDp = 0.dp,
                    onMapLoadedCallback = onMapLoaded
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        AnimatedVisibility(
                            visible = runState.isAct,
                            exit = androidx.compose.animation.fadeOut()
                        ) {
                            IconButton(
                                onClick = onBackClick,
                                modifier = Modifier.background(
                                    colorScheme.surface,
                                    shape = shape
                                )
                            ) {
                                Icon(
                                    imageVector = arrowLeft,
                                    contentDescription = null
                                )
                            }
                        }
                        IconButton(
                            onClick = { },
                            modifier = Modifier.background(
                                colorScheme.surface,
                                shape = shape
                            )
                        ) {
                            GpsStrengthIndicator(
                                strength = gpsStrength
                            )
                        }
                    }

                    IconButton(
                        onClick = { moveToUserTrigger++ },
                        enabled = context.hasPreciseForegroundLocationPermission(),
                        modifier = Modifier.background(
                            colorScheme.surface,
                            shape = shape
                        )
                    ) {
                        Icon(
                            imageVector = com.rk.pace.theme.location,
                            contentDescription = null
                        )
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = !mapLoaded,
            exit = androidx.compose.animation.fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        colorScheme.surface
                    )
                    .zIndex(2f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}