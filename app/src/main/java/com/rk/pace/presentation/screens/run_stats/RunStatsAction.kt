package com.rk.pace.presentation.screens.run_stats

sealed interface RunStatsAction {

    data object OnDeleteRunClick : RunStatsAction

}