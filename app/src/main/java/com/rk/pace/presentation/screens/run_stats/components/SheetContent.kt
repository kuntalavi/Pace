package com.rk.pace.presentation.screens.run_stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.rk.pace.domain.model.PacePoint
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.Split
import com.rk.pace.presentation.charts.SplitChart
import com.rk.pace.presentation.screens.stats.components.PaceStat
import com.rk.pace.presentation.theme.space
import com.rk.pace.presentation.ut.FormatUt.formatDistance
import com.rk.pace.presentation.ut.FormatUt.formatDuration
import com.rk.pace.presentation.ut.FormatUt.formatPace

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetContent(
    run: Run,
    splits: List<Split>,
    paceChartData: List<PacePoint>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(
                horizontal = space.large
            )
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = run.title.uppercase().ifEmpty { "RUNNING" },
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp
        )

        Spacer(
            modifier = Modifier.height(
                space.large
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                space.medium
            )
        ) {
            PaceStat(
                modifier = Modifier.weight(1f),
                title = "DISTANCE",
                value = formatDistance(run.distanceMeters),
                unit = "KM"
            )
            PaceStat(
                modifier = Modifier.weight(1f),
                title = "TIME",
                value = formatDuration(run.durationMilliseconds)
            )
        }

        Spacer(
            modifier = Modifier.height(
                space.large
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                space.medium
            )
        ) {
            PaceStat(
                modifier = Modifier.weight(1f),
                title = "AVG PACE",
                value = formatPace(run.avgSpeedMps),
                unit = "/KM"
            )
            PaceStat(
                modifier = Modifier.weight(1f),
                title = "AVG SPEED",
                value = "%.2f".format(run.avgSpeedMps),
                unit = "MPS"
            )
        }

        Spacer(
            modifier = Modifier.height(
                space.medium
            )
        )

        SplitChart(
            splits
        )

        Spacer(
            modifier = Modifier.height(
                space.medium
            )
        )

//        PaceChart(
//            data = paceChartData
//        )

//        Spacer(
//            modifier = Modifier.height(
//                space.large
//            )
//        )

    }
}
