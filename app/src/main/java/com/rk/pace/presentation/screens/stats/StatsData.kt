package com.rk.pace.presentation.screens.stats

import com.rk.pace.domain.model.DayDistance
import com.rk.pace.domain.model.GoalProgress
import com.rk.pace.domain.model.GoalType
import com.rk.pace.domain.model.WeekGoals
import com.rk.pace.domain.model.WeekGoalsProgress
import com.rk.pace.domain.model.WeekStats

data class StatsData(

    val weekStats: WeekStats = WeekStats(0, 0f, 0L, 0f),
    val weekGoals: WeekGoals = WeekGoals(null, null, null),
    val weekGoalsProgress: WeekGoalsProgress =
        WeekGoalsProgress(
            runGoalProgress = GoalProgress(
                GoalType.RUNS,
                0f,
                0f,
                false
            ),
            distanceGoalProgress = GoalProgress(
                GoalType.DISTANCE_METERS,
                0f,
                0f,
                false
            ),
            durationGoalProgress = GoalProgress(
                GoalType.DURATION_MILLISECONDS,
                0f,
                0f,
                false
            )
        ),
    val weekDistanceChartData: List<DayDistance> = emptyList()

)
