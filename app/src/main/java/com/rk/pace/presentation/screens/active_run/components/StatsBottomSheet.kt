package com.rk.pace.presentation.screens.active_run.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rk.pace.common.extension.formatDistance
import com.rk.pace.common.extension.formatTime
import com.rk.pace.common.ut.PaceUt.getPace
import com.rk.pace.domain.model.RunState
import com.rk.pace.presentation.components.ButtonBox
import com.rk.pace.presentation.components.StatItem
import com.rk.pace.theme.White
import kotlinx.coroutines.delay

data class StatPage(
    val title: String,
    val v: String
)

@Composable
fun StatsBottomSheet(
    runState: RunState,
    start: () -> Unit,
    pause: () -> Unit,
    resume: () -> Unit,
    stop: () -> Unit
) {

    val stats = listOf(
        StatPage("DISTANCE (km)", runState.distanceMeters.formatDistance()),
        StatPage("T", runState.durationMilliseconds.formatTime()),
        StatPage("AVG PACE (m/km)", getPace(runState.avgSpeedMps))
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
        Modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp)
            .background(White)
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
//                .height(130.dp)
        ) { page ->
            val stat = stats[page]
            StatItem(
                title = stat.title,
                value = stat.v
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (!runState.isAct) {
            ButtonBox(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    start()
                },
                text = "START RUN"
            )
        }
        if (runState.isAct && !runState.paused) {
            ButtonBox(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    pause()
                },
                text = "PAUSE"
            )
        }
        if (runState.isAct && runState.paused) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ButtonBox(
                    modifier = Modifier.fillMaxWidth(.4f),
                    onClick = {
                        resume()
                    },
                    text = "RESUME"
                )
                ButtonBox(
                    modifier = Modifier.fillMaxWidth(.6f),
                    onClick = {
                        stop()
                    },
                    text = "STOP"
                )
            }
        }
    }
}