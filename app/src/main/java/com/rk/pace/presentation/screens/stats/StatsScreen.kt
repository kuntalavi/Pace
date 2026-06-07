package com.rk.pace.presentation.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.presentation.screens.stats.components.DistanceCard
import com.rk.pace.presentation.screens.stats.components.PaceStat
import com.rk.pace.presentation.screens.stats.components.WeekNavigator
import com.rk.pace.presentation.theme.space
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

    val weekStats = state.data.weekStats
    val weekDistanceChartData = state.data.weekDistanceChartData

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    space.medium
                )
                .verticalScroll(rememberScrollState())
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
                modifier = modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = space.medium,
                        vertical = space.large
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PaceStat(
                    title = "AVG PACE",
                    value = formatPace(weekStats.avgSpeedMps),
                    unit = "/KM"
                )

                PaceStat(
                    title = "DURATION",
                    value = formatDuration(weekStats.durationMilliseconds)
                )

                Spacer(
                    modifier =
                        Modifier.width(0.dp)
                )
            }

        }

    }

}
