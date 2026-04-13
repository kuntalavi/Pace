package com.rk.pace.presentation.screens.run_stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rk.pace.domain.model.PacePoint
import com.rk.pace.domain.model.RunWithPath
import com.rk.pace.domain.model.Split
import com.rk.pace.presentation.charts.PaceChart
import com.rk.pace.presentation.charts.SplitChart
import com.rk.pace.presentation.components.PaceStatCard
import com.rk.pace.presentation.ut.FormatUt.formatDistance
import com.rk.pace.presentation.ut.FormatUt.formatDuration
import com.rk.pace.presentation.ut.FormatUt.formatPace
import kotlin.text.ifEmpty

@Composable
fun SheetContent(
    run: RunWithPath,
    splits: List<Split>,
    paceChartData: List<PacePoint>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = run.run.title.uppercase().ifEmpty { "RUNNING" },
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PaceStatCard(
                label = "DISTANCE",
                value = formatDistance(run.run.distanceMeters),
                unit = "KM",
                modifier = Modifier.weight(1f)
            )
            PaceStatCard(
                label = "TIME",
                value = formatDuration(run.run.durationMilliseconds),
                unit = "",
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PaceStatCard(
                label = "AVG PACE",
                value = formatPace(run.run.avgSpeedMps),
                unit = "/KM",
                modifier = Modifier.weight(1f)
            )
            PaceStatCard(
                label = "AVG SPEED",
                value = "%.2f".format(run.run.avgSpeedMps),
                unit = "MPS",
                modifier = Modifier.weight(1f)
            )
        }

        SplitChart(
            splits
        )

        PaceChart(
            data = paceChartData
        )
    }
}