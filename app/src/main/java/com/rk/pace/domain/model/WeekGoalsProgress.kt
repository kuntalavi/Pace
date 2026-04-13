package com.rk.pace.domain.model

data class WeekGoalsProgress(
    val runGoalProgress: GoalProgress,
    val distanceGoalProgress: GoalProgress,
    val durationGoalProgress: GoalProgress
)
