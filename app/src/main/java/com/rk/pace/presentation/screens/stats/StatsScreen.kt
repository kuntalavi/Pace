package com.rk.pace.presentation.screens.stats

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.presentation.components.PaceStatCard
import com.rk.pace.presentation.screens.stats.components.DistanceCard
import com.rk.pace.presentation.screens.stats.components.GoalsPager
import com.rk.pace.presentation.screens.stats.components.WeekNavigator
import com.rk.pace.presentation.ut.FormatUt.formatDistance
import com.rk.pace.presentation.ut.FormatUt.formatDuration
import com.rk.pace.presentation.ut.FormatUt.formatPace
import com.rk.pace.presentation.theme.Black
import com.rk.pace.presentation.theme.add

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatsScreen(
    modifier: Modifier = Modifier,
    viewModel: StatsViewModel = hiltViewModel(),
    onAddGoalClick: () -> Unit,
    onGoalClick: () -> Unit
) {

    val weekDistanceChartData by viewModel.weekDistanceChartData.collectAsStateWithLifecycle()
    val weekStats by viewModel.weekStats.collectAsStateWithLifecycle()
    val weekLabel by viewModel.weekLabel.collectAsStateWithLifecycle()
    val canGoForward by viewModel.canGoForward.collectAsStateWithLifecycle()
    val weekGoalsProgress by viewModel.weekGoalsProgress.collectAsStateWithLifecycle()

    val (runGoalProgress, distanceGoalProgress, durationGoalProgress) = weekGoalsProgress

    val isAnyGoalSet = runGoalProgress.isSet || distanceGoalProgress.isSet || durationGoalProgress.isSet
    val areAllGoalsSet = runGoalProgress.isSet && distanceGoalProgress.isSet && durationGoalProgress.isSet

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 15.dp)
                .verticalScroll(rememberScrollState()),
//            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            WeekNavigator(
                weekLabel = weekLabel,
                canGoForward = canGoForward,
                onPrevious = { viewModel.goToPreviousWeek() },
                onNext = { viewModel.goToNextWeek() }
            )

            DistanceCard(
                distance = formatDistance(weekStats.distanceMeters),
                weekDistanceChartData = weekDistanceChartData
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PaceStatCard(
                    label = "AVG PACE",
                    value = formatPace(weekStats.avgSpeedMps),
                    unit = "/KM",
                    modifier = Modifier.weight(1f)
                )
                PaceStatCard(
                    label = "TOTAL TIME",
                    value = formatDuration(weekStats.durationMilliseconds),
                    unit = "",
                    modifier = Modifier.weight(1f)
                )
            }

            if (isAnyGoalSet) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "YOUR GOALS",
                        style = MaterialTheme.typography.titleLarge
                    )

                    IconButton(
                        onClick = { onAddGoalClick() },
                        enabled = !areAllGoalsSet
                    ) {
                        Icon(
                            imageVector = add,
                            contentDescription = null,
                            tint = if (areAllGoalsSet) MaterialTheme.colorScheme.background else Black
                        )
                    }
                }

                GoalsPager(
                    weekGoalsProgress = weekGoalsProgress,
                    onGoalClick = { onGoalClick() }
                )

            } else {
                Text(
                    text = "MAKE A GOAL",
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(
                    onClick = { onAddGoalClick() }
                ) {
                    Icon(
                        imageVector = add,
                        contentDescription = null
                    )
                }
            }
        }

    }

}