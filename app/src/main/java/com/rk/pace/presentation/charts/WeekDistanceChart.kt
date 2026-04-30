package com.rk.pace.presentation.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rk.pace.domain.model.DayDistance
import com.rk.pace.presentation.theme.Black

@Composable
fun WeekDistanceChart(
    modifier: Modifier = Modifier,
    data: List<DayDistance>,
    barColor: Color = MaterialTheme.colorScheme.primary,
    todayColor: Color = MaterialTheme.colorScheme.secondary,
) {
    val maxDistance = data.maxOfOrNull { it.distanceMeters } ?: 1f

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val barAreaTop = 16.dp.toPx()

            val totalBars = data.size
            val horizontalPadding = 12.dp.toPx()
            val slotWidth = (canvasWidth - horizontalPadding * 2) / totalBars
            val barWidth = slotWidth * 0.2f
            val barMaxHeight = canvasHeight - barAreaTop
            val cornerRadius = 3.dp.toPx()

            data.forEachIndexed { index, dayData ->
                val barHeight = (dayData.distanceMeters / maxDistance) * barMaxHeight
                val slotCenterX = horizontalPadding + slotWidth * index + slotWidth / 2
                val left = slotCenterX - barWidth / 2
                val top = canvasHeight - barHeight

                val color = if (dayData.isToday) todayColor else barColor

                drawRoundRect(
                    color = color,
                    topLeft = Offset(left, top),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
        ) {
            data.forEach { dayData ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = dayData.day,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (dayData.isToday) todayColor else Black,
                        fontWeight = if (dayData.isToday) FontWeight.Bold else FontWeight.Normal,
                    )
                }
            }
        }
    }
}