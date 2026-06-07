package com.rk.pace.presentation.screens.active_run.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rk.pace.domain.model.RunState
import com.rk.pace.presentation.components.ButtonSize
import com.rk.pace.presentation.components.ButtonVariant
import com.rk.pace.presentation.components.PaceButton
import com.rk.pace.presentation.theme.space
import com.rk.pace.presentation.ut.FormatUt.formatDistance
import com.rk.pace.presentation.ut.FormatUt.formatDuration
import com.rk.pace.presentation.ut.FormatUt.formatPace

@Composable
fun RunFullSheet(
    runState: RunState,
    mapLoaded: Boolean,
    start: () -> Unit,
    pause: () -> Unit,
    resume: () -> Unit,
    stop: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            PaceRunStat(
                title = "DISTANCE",
                value = formatDistance(runState.distanceMeters),
                unit = "KM"
            )
            PaceRunStat(
                title = "AVG PACE",
                value = formatPace(runState.avgSpeedMps),
                unit = "/KM"
            )
            PaceRunStat(
                title = "DURATION",
                value = formatDuration(runState.durationMilliseconds),
            )

            if (!runState.isAct) {
                PaceButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "START",
                    onClick = {
                        start()
                    },
                    variant = ButtonVariant.Filled,
                    enabled = mapLoaded,
                    size = ButtonSize.LARGE
                )
            } else if (!runState.paused) {
                PaceButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "PAUSE",
                    onClick = {
                        pause()
                    },
                    variant = ButtonVariant.Filled,
                    size = ButtonSize.LARGE
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(
                        space.small
                    )
                ) {
                    PaceButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = "RESUME",
                        onClick = {
                            resume()
                        },
                        variant = ButtonVariant.Filled,
                        size = ButtonSize.LARGE
                    )
                    PaceButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = "STOP",
                        onClick = {
                            stop()
                        },
                        variant = ButtonVariant.Tonal,
                        size = ButtonSize.LARGE
                    )
                }
            }
        }
    }
}