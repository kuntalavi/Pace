package com.rk.pace.presentation.screens.active_run.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay

data class StatPage(
    val label: String,
    val value: String,
    val unit: String = ""
)

@Composable
fun RunBottomSheet(
    modifier: Modifier = Modifier,
    runState: RunState,
    mapLoaded: Boolean,
    start: () -> Unit,
    pause: () -> Unit,
    resume: () -> Unit,
    stop: () -> Unit
) {

    val stats = listOf(
        StatPage("DISTANCE", formatDistance(runState.distanceMeters), "KM"),
        StatPage("AVG PACE", formatPace(runState.avgSpeedMps), "/KM"),
        StatPage("DURATION", formatDuration(runState.durationMilliseconds))
    )

    val pagerState = rememberPagerState(pageCount = { stats.size })

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000L)
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space.large
            )
        ) {

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
                    PaceRunStat(
                        title = stat.label,
                        value = stat.value,
                        unit = stat.unit
                    )
                }
            }

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
                        text = "RESUME",
                        onClick = {
                            resume()
                        },
                        variant = ButtonVariant.Filled,
                        size = ButtonSize.LARGE
                    )
                    PaceButton(
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
