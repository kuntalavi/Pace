package com.rk.pace.presentation.screens.active_run.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rk.pace.common.extension.formatDistance
import com.rk.pace.common.extension.formatTime
import com.rk.pace.common.ut.PaceUt.getPace
import com.rk.pace.domain.model.RunState
import com.rk.pace.domain.tracking.GpsStrength
import com.rk.pace.presentation.components.PaceButton
import com.rk.pace.presentation.components.StatItem
import kotlinx.coroutines.delay

data class StatPage(
    val title: String,
    val value: String
)

@Composable
fun RunBottomSheet(
    gpsStrength: GpsStrength,
    runState: RunState,
    isMapLoaded: Boolean,
    start: () -> Unit,
    pause: () -> Unit,
    resume: () -> Unit,
    stop: () -> Unit
) {

    val stats = listOf(
        StatPage("DISTANCE", runState.distanceMeters.formatDistance()),
        StatPage("DURATION", runState.durationMilliseconds.formatTime()),
        StatPage("AVG PACE", getPace(runState.avgSpeedMps))
    )

    val pagerState = rememberPagerState(pageCount = { stats.size })

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000L)
            val nextPage = (pagerState.currentPage + 1) % stats.size
            pagerState.animateScrollToPage(
                nextPage,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = LinearOutSlowInEasing
                )
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {

        GpsStrengthIndicator(
            strength = gpsStrength
        )

        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth(),
            state = pagerState,
            verticalAlignment = Alignment.CenterVertically
        ) { page ->
            val stat = stats[page]
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                StatItem(
                    title = stat.title,
                    value = stat.value
                )
            }
        }

        if (!runState.isAct) {
            PaceButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "START RUN",
                onClick = {
                    start()
                },
                filled = true,
                enabled = isMapLoaded
            )
        }
        if (runState.isAct && !runState.paused) {
            PaceButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "PAUSE",
                onClick = {
                    pause()
                },
                filled = true
            )
        }
        if (runState.isAct && runState.paused) {
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
                    filled = true
                )
                PaceButton(
                    modifier = Modifier.fillMaxWidth(.6f),
                    text = "STOP",
                    onClick = {
                        stop()
                    },
                    filled = true
                )
            }
        }

    }
}