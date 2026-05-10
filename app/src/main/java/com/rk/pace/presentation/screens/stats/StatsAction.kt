package com.rk.pace.presentation.screens.stats

import com.rk.pace.domain.model.WeekGoals

sealed interface StatsAction {

    data object OnPreviousWeekClick : StatsAction
    data object OnNextWeekClick : StatsAction
    data class OnUpdateGoals(
        val weekGoals: WeekGoals
    ) : StatsAction

    data object OnAddGoalClick: StatsAction
    data object OnGoalClick : StatsAction

}