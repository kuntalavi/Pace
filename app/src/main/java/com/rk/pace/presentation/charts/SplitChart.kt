package com.rk.pace.presentation.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rk.pace.common.ut.PaceUt.formatPace
import com.rk.pace.domain.model.Split

@Composable
fun SplitChart(
    splits: List<Split>,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary
) {
    if (splits.isEmpty()) return

    val maxDuration = splits.maxOf { it.durationMilliseconds }
    val minDuration = splits.minOf { it.durationMilliseconds }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp)
    ) {
        Text(
            text = "SPLITS",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Km",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.width(30.dp)
            )
            Text(
                text = "Pace",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.width(60.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(5.dp))

        splits.forEach { split ->
            SplitRow(
                split,
                maxDuration,
                minDuration,
                barColor = barColor,
            )
        }
    }
}

@Composable
private fun SplitRow(
    split: Split,
    maxDuration: Long,
    minDuration: Long,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val index = if (split.distanceMeters != 1000f) {
            "%.1f".format(split.distanceMeters / 1000f)
        } else {
            "${split.index}"
        }

        Text(
            text = index,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.width(30.dp)
        )

        Text(
            text = formatPace(split.paceSeconds),
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.width(60.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        val range = (maxDuration - minDuration).toFloat()

        val minBarWidthPercent = 15f
        val maxBarWidthPercent = 100f
        val normalizedValue = if (range > 0) {
            ((maxDuration - split.durationMilliseconds) / range)
        } else {
            .75f
        }

        val barWidthPercent = minBarWidthPercent + (normalizedValue * (maxBarWidthPercent - minBarWidthPercent))
        Box(
            modifier = Modifier
                .height(18.dp)
                .fillMaxWidth(barWidthPercent / 100)
                .background(
                    color = barColor,
                    shape = RoundedCornerShape(4.dp)
                )
        )
    }
}