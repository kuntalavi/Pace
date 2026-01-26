package com.rk.pace.presentation.screens.run_stats

import com.rk.pace.domain.model.RunWithPath

sealed class RunStatsState {
    class Load : RunStatsState()
    class Success(val runWithPath: RunWithPath) : RunStatsState()
    class Error(val message: String) : RunStatsState()
}