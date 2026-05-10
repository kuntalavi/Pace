package com.rk.pace.presentation.screens.run_stats

sealed interface RunStatsEvent {

    data object RunDeleted : RunStatsEvent
    data class Error(val message: String) : RunStatsEvent

}