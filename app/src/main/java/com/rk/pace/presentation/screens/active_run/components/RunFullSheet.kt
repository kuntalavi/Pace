package com.rk.pace.presentation.screens.active_run.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rk.pace.domain.model.RunState
import com.rk.pace.presentation.components.ButtonSize
import com.rk.pace.presentation.components.ButtonVariant
import com.rk.pace.presentation.components.PaceButton
import com.rk.pace.presentation.components.PaceStatItem
import com.rk.pace.presentation.components.StatItemStyle
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
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            PaceStatItem(
                value = formatDistance(runState.distanceMeters),
                label = "DISTANCE",
                unit = "KM",
                style = StatItemStyle.Hero
            )
            PaceStatItem(
                value = formatDuration(runState.durationMilliseconds),
                label = "DURATION",
                style = StatItemStyle.Hero
            )
            PaceStatItem(
                value = formatPace(runState.avgSpeedMps),
                label = "AVG PACE",
                unit = "/KM",
                style = StatItemStyle.Hero
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    PaceButton(
                        modifier = Modifier.fillMaxWidth(.4f),
                        text = "RESUME",
                        onClick = {
                            resume()
                        },
                        variant = ButtonVariant.Filled,
                        size = ButtonSize.LARGE
                    )
                    PaceButton(
                        modifier = Modifier.fillMaxWidth(.6f),
                        text = "STOP",
                        onClick = {
                            stop()
                        },
                        variant = ButtonVariant.Filled,
                        size = ButtonSize.LARGE
                    )
                }
            }
        }
    }
}