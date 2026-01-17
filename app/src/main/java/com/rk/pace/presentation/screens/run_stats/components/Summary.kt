package com.rk.pace.presentation.screens.run_stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rk.pace.common.extension.formatDistance
import com.rk.pace.common.extension.formatTime
import com.rk.pace.common.ut.PaceUt.getPace
import com.rk.pace.presentation.components.StatItem

@Composable
fun Summary(
    distance: Float,
    time: Long,
    speed: Float
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SUMMARY",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        StatItem(title = "DISTANCE (km)", value = distance.formatDistance())
        StatItem(title = "T", value = time.formatTime())
        StatItem(
            title = "AVG PACE (m/km)",
            value = getPace(speed)
        )
    }
}