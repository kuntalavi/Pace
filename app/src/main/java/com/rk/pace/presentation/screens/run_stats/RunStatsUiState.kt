package com.rk.pace.presentation.screens.run_stats

data class RunStatsUiState(

    val isCurrentUser: Boolean = false,
    val load: Boolean = true,

    val data: RunStatsData? = null,

    val error: String? = null

)