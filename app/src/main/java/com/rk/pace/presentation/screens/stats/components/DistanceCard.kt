package com.rk.pace.presentation.screens.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rk.pace.domain.model.DayDistance
import com.rk.pace.presentation.charts.WeekDistanceChart
import com.rk.pace.presentation.theme.Shapes
import com.rk.pace.presentation.theme.scheme
import com.rk.pace.presentation.theme.space

@Composable
fun DistanceCard(
    distance: String,
    weekDistanceChartData: List<DayDistance>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = scheme.background
        ),
        shape = Shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.padding(
                    space.medium
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PaceStat(
                    modifier = Modifier.weight(1f),
                    title = "TOTAL DISTANCE",
                    value = distance,
                    unit = "KM"
                )
                Icon(
                    imageVector = com.rk.pace.presentation.theme.distance,
                    contentDescription = null,
                    tint = scheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }

            if (distance != "0") {
                WeekDistanceChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    data = weekDistanceChartData
                )
            }
        }
    }
}