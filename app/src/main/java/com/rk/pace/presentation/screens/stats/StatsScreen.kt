package com.rk.pace.presentation.screens.stats

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
import com.rk.pace.presentation.components.PaceStat
import com.rk.pace.presentation.screens.stats.components.DistanceCard
import com.rk.pace.presentation.screens.stats.components.GoalsPager
import com.rk.pace.presentation.screens.stats.components.WeekNavigator
import com.rk.pace.presentation.theme.Black
import com.rk.pace.presentation.theme.add
import com.rk.pace.presentation.ut.FormatUt.formatDistance
import com.rk.pace.presentation.ut.FormatUt.formatDuration
import com.rk.pace.presentation.ut.FormatUt.formatPace
import com.rk.pace.presentation.ut.ObserveAsEvents

@Composable
fun StatsScreen(
    modifier: Modifier = Modifier,
    viewModel: StatsViewModel = hiltViewModel(),
    onAddGoalClick: () -> Unit,
    onGoalClick: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is StatsEvent.GoToAddGoal -> onAddGoalClick()
            is StatsEvent.GoToGoal -> onGoalClick()
            else -> {}
        }
    }

    val weekGoalsProgress = state.data.weekGoalsProgress
    val (
        runGoalProgress,
        distanceGoalProgress,
        durationGoalProgress
    ) =
        weekGoalsProgress
    val weekStats = state.data.weekStats
    val weekDistanceChartData = state.data.weekDistanceChartData

    val isAnyGoalSet =
        runGoalProgress.isSet || distanceGoalProgress.isSet || durationGoalProgress.isSet
    val areAllGoalsSet =
        runGoalProgress.isSet && distanceGoalProgress.isSet && durationGoalProgress.isSet

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
                weekLabel = state.weekLabel,
                canGoForward = state.canGoForward,
                onAction = viewModel::onAction
            )

            DistanceCard(
                distance = formatDistance(weekStats.distanceMeters),
                weekDistanceChartData = weekDistanceChartData
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                PaceStat(
                    modifier = Modifier.weight(1f),
                    label = "AVG PACE",
                    value = formatPace(weekStats.avgSpeedMps),
                    unit = "/KM"
                )
                PaceStat(
                    modifier = Modifier.weight(1f),
                    label = "TOTAL TIME",
                    value = formatDuration(weekStats.durationMilliseconds)
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
                    onGoalClick = {
                        viewModel.onAction(
                            StatsAction.OnGoalClick
                        )
                    }
                )

            } else {
                Text(
                    text = "MAKE A GOAL",
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(
                    onClick = {
                        viewModel.onAction(
                            StatsAction.OnAddGoalClick
                        )
                    }
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