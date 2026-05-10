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
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.Split
import com.rk.pace.presentation.charts.PaceChart
import com.rk.pace.presentation.charts.SplitChart
import com.rk.pace.presentation.components.PaceStat
import com.rk.pace.presentation.ut.FormatUt.formatDistance
import com.rk.pace.presentation.ut.FormatUt.formatDuration
import com.rk.pace.presentation.ut.FormatUt.formatPace

@Composable
fun SheetContent(
    run: Run,
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
            text = run.title.uppercase().ifEmpty { "RUNNING" },
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PaceStat(
                modifier = Modifier.weight(1f),
                label = "DISTANCE",
                value = formatDistance(run.distanceMeters),
                unit = "KM"
            )
            PaceStat(
                modifier = Modifier.weight(1f),
                label = "TIME",
                value = formatDuration(run.durationMilliseconds),
                unit = ""
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PaceStat(
                modifier = Modifier.weight(1f),
                label = "AVG PACE",
                value = formatPace(run.avgSpeedMps),
                unit = ""
            )
            PaceStat(
                modifier = Modifier.weight(1f),
                label = "AVG SPEED",
                value = "%.2f".format(run.avgSpeedMps),
                unit = "MPS"
            )
        }

        SplitChart(
            splits
        )

        PaceChart(
            data = paceChartData
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )
    }
}