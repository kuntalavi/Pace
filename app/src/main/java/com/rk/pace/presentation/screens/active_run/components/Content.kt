package com.rk.pace.presentation.screens.active_run.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.BottomSheetScaffold
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rk.pace.data.ut.hasPreciseForegroundLocationPermission
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.model.RunState
import com.rk.pace.domain.tracker.GpsStrength
import com.rk.pace.presentation.theme.Gray
import com.rk.pace.presentation.theme.arrowLeft
import com.rk.pace.presentation.theme.navigationFilled
import com.rk.pace.presentation.theme.shape

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(
    warning: String?,
    runState: RunState,
    location: RunPathPoint?,
    gpsStrength: GpsStrength,
    onStartRun: () -> Unit,
    onResumeRun: () -> Unit,
    onPauseRun: () -> Unit,
    onStopRun: () -> Unit,
    onBack: () -> Unit
) {

    var mapLoaded by rememberSaveable { mutableStateOf(false) }

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true,
            confirmValueChange = { true }
        )
    )
    val expanded = scaffoldState.bottomSheetState.targetValue == SheetValue.Expanded
    val context = LocalContext.current
    var moveToUserTrigger by remember { mutableIntStateOf(0) }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContainerColor = colorScheme.surface,
        containerColor = colorScheme.surface,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorScheme.surface)
                    .windowInsetsPadding(
                        if (expanded) WindowInsets.statusBars else
                            WindowInsets.navigationBars
                    )
                    .padding(
                        20.dp
                    )
            ) {
                if (expanded) {
                    RunFullSheet(
                        runState = runState,
                        mapLoaded = mapLoaded,
                        start = onStartRun,
                        pause = onPauseRun,
                        resume = onResumeRun,
                        stop = onStopRun
                    )
                } else {
                    RunBottomSheet(
                        runState = runState,
                        mapLoaded = mapLoaded,
                        start = onStartRun,
                        pause = onPauseRun,
                        resume = onResumeRun,
                        stop = onStopRun
                    )
                }
            }
        },
        sheetDragHandle = { },
        sheetShape = RectangleShape,
        sheetPeekHeight = 250.dp,
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
                location = location,
                moveToUserTrigger = moveToUserTrigger,
                isAct = runState.isAct,
                paused = runState.paused
            ) { mapLoaded = true }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(15.dp)
            ) {

                AnimatedVisibility(
                    visible = warning != null,
                    enter = expandVertically(animationSpec = tween(300)),
                    exit = shrinkVertically(animationSpec = tween(300))
                ) {
                    warning?.let {
                        WarnBanner(
                            message = it
                        )
                    }
                }
                AnimatedVisibility(
                    visible = warning == null,
                    enter = expandVertically(animationSpec = tween(300)),
                    exit = shrinkVertically(animationSpec = tween(300))
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
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
                                    onClick = onBack,
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
                                imageVector = navigationFilled,
                                contentDescription = null,
                                tint = Gray
                            )
                        }
                    }
                }
            }
        }
    }
}
