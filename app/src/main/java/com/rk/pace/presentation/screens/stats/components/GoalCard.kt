package com.rk.pace.presentation.screens.stats.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rk.pace.domain.model.GoalProgress
import com.rk.pace.domain.model.GoalType
import com.rk.pace.presentation.charts.GoalPieChart
import com.rk.pace.presentation.components.PaceStatCard
import com.rk.pace.presentation.ut.FormatUt.formatDistance
import com.rk.pace.presentation.ut.FormatUt.formatDuration

@Composable
fun GoalCard(
    progress: GoalProgress,
    onGoalClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val label = when (progress.type) {
        GoalType.RUNS -> "RUN ${progress.goal.toInt()} TIMES / WEEK"
        GoalType.DISTANCE_METERS -> "RUN ${formatDistance(progress.goal)} KM PER WEEK"
        GoalType.DURATION_MILLISECONDS -> "RUN FOR ${formatDuration(progress.goal.toLong())} /WEEK"
    }
    val progressValue = when (progress.type) {
        GoalType.RUNS -> "${progress.progress.toInt()}"
        GoalType.DISTANCE_METERS -> formatDistance(progress.progress)
        GoalType.DURATION_MILLISECONDS -> formatDuration(progress.progress.toLong())
    }
    val goal = when (progress.type) {
        GoalType.RUNS -> "of ${progress.goal.toInt()} Runs"
        GoalType.DISTANCE_METERS -> "of ${formatDistance(progress.goal)} Km"
        GoalType.DURATION_MILLISECONDS -> "of ${formatDuration(progress.goal.toLong())}"
    }

    val goalValue = when (progress.type) {
        GoalType.RUNS -> "${progress.goal.toInt()}"
        GoalType.DISTANCE_METERS -> formatDistance(progress.goal)
        GoalType.DURATION_MILLISECONDS -> formatDuration(progress.goal.toLong())
    }
    val unit = when (progress.type) {
        GoalType.RUNS -> ""
        GoalType.DISTANCE_METERS -> "KM"
        GoalType.DURATION_MILLISECONDS -> ""
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onGoalClick() }
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,

        ){
            PaceStatCard(
                label = "PROGRESS",
                value = progressValue,
                unit = unit,
                        modifier = Modifier.weight(1f)
            )
            PaceStatCard(
                label = "GOAL",
                value = goalValue,
                unit = unit,
                modifier = Modifier.weight(1f)
            )
        }

//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceAround,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Row(
//                    verticalAlignment = Alignment.Bottom
//                ) {
//                    Text(
//                        text = progressLabel,
//                        color = MaterialTheme.colorScheme.primary,
//                        style = MaterialTheme.typography.headlineMedium
//                    )
//                    if (progressLabel != "-"){
//                        Spacer(
//                            modifier = Modifier.width(5.dp)
//                        )
//                        Text(
//                            text = unit,
//                            color = MaterialTheme.colorScheme.secondary,
//                            style = MaterialTheme.typography.labelSmall,
//                            modifier = Modifier
//                                .padding(bottom = 6.dp)
//                        )
//                    }
//                }
//                if (progressLabel != "-") {
//                    Text(
//                        text = goal,
//                        style = MaterialTheme.typography.labelSmall
//                    )
//                }
//            }

        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            GoalPieChart(
                progress = progress
            )
            Text(
                text = "${(progress.fraction * 100).toInt()}%",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
