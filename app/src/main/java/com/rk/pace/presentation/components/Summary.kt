package com.rk.pace.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rk.pace.common.extension.formatDistance
import com.rk.pace.common.extension.formatTime
import com.rk.pace.common.ut.PaceUt.getPace
import com.rk.pace.domain.model.Run
import java.util.Locale

@Composable
fun Summary(
    run: Run
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(
            text = "SUMMARY",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatItem(
                title = "DISTANCE",
                value = run.distanceMeters.formatDistance()
            )
            StatItem(
                title = "DURATION",
                value = run.durationMilliseconds.formatTime()
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatItem(
                title = "AVG PACE",
                value = getPace(run.avgSpeedMps)
            )
            StatItem(
                title = "AVG SPEED",
                value = String.format(Locale.getDefault(), "%.1f", run.avgSpeedMps) + " M/s"
            )
        }
    }
}