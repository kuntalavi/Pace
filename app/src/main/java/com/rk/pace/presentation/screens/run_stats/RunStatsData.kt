package com.rk.pace.presentation.screens.run_stats

import com.rk.pace.domain.model.PacePoint
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.model.Split

data class RunStatsData(
    val run: Run,
    val path: List<RunPathPoint>,
    val splits: List<Split>,
    val paceChartData: List<PacePoint>
)
