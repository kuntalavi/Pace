package com.rk.pace.presentation.screens.stats

data class StatsUiState(

    val weekOffset: Int = 0,
    val weekLabel: String = "This Week",
    val canGoForward: Boolean = false,

    val data: StatsData = StatsData()

)
