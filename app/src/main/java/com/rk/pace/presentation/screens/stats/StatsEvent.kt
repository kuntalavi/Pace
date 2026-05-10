package com.rk.pace.presentation.screens.stats

sealed interface StatsEvent {

    data object GoToAddGoal : StatsEvent
    data object GoToGoal : StatsEvent
    data object OnBack: StatsEvent

}