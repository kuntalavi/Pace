package com.rk.pace.presentation.screens.stats.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rk.pace.domain.model.WeekGoalsProgress

@Composable
fun GoalsPager(
    weekGoalsProgress: WeekGoalsProgress,
    onGoalClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (runGoalProgress, distanceGoalProgress, durationGoalProgress) = weekGoalsProgress

    val pagerState = rememberPagerState(pageCount = { 3 })
    val pages = listOf(runGoalProgress, distanceGoalProgress, durationGoalProgress)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            GoalCard(
                progress = pages[page],
                onGoalClick = onGoalClick
            )
        }
    }
}