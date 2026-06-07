package com.rk.pace.presentation.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.rk.pace.domain.model.PacePoint

@Composable
fun PaceChart(
    data: List<PacePoint>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {

    val paddingStart = 10f
    val paddingEnd = 10f
    val paddingTop = 40f
    val paddingBottom = 60f

    val textMeasurer = rememberTextMeasurer()

    val labelStyle = MaterialTheme.typography.labelSmall

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "PACE", style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(horizontal = 10.dp)
        ) {
            if (data.isEmpty()) return@Canvas
            val width = size.width - paddingStart - paddingEnd
            val height = size.height - paddingTop - paddingBottom

            val minPace = data.minOf { it.paceMinPerKm }
            val maxPace = data.maxOf { it.paceMinPerKm }
            val paceRange = if (maxPace == minPace) 1f else maxPace - minPace

            val maxDistance = data.maxOf { it.distanceKm }
            val distanceRange = if (maxDistance == 0f) 1f else maxDistance

            fun getX(distance: Float) = paddingStart + (distance / distanceRange) * width

            fun getY(pace: Float): Float {
                val clamped = pace.coerceIn(minPace, maxPace)
                val ratio = (clamped - minPace) / paceRange
                return paddingTop + (ratio * height)
            }

            val points = data.map {
                Offset(
                    x = getX(it.distanceKm), y = getY(it.paceMinPerKm)
                )
            }

            val linePath = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 1 until points.size) {
                    val prev = points[i - 1]
                    val curr = points[i]
                    val midX = (prev.x + curr.x) / 2

                    cubicTo(
                        midX, prev.y, midX, curr.y, curr.x, curr.y
                    )
                }
            }

            val fillPath = Path().apply {
                addPath(linePath)
                lineTo(points.last().x, size.height - paddingBottom)
                lineTo(points.first().x, size.height - paddingBottom)
                close()
            }

            // --- 1. DRAW AREA GRADIENT ---
            drawPath(
                path = fillPath, brush = Brush.verticalGradient(
                    colors = listOf(
                        lineColor.copy(alpha = 0.6f),
                        lineColor.copy(alpha = 0.3f),
                        Color.Transparent
                    ), startY = paddingTop, endY = size.height - paddingBottom
                )
            )

            // --- 2. DRAW MAIN CURVE ---
            drawPath(
                path = linePath, color = lineColor, style = Stroke(
                    width = 2.5.dp.toPx(), cap = StrokeCap.Round
                )
            )

            // --- 3. Y-AXISLABELS (Minimalist: Only 3 labels) ---
            // val ySteps = 2
            // repeat(ySteps + 1) { i ->
            // val pace = minPace + (paceRange / ySteps) * i
            // val y = getY(pace)
            //

            // val textLayoutResult = textMeasurer.measure(
            // text = formatPace(pace),
            // style = labelStyle
            // )
            // drawText(
            // textLayoutResult = textLayoutResult,
            // topLeft = Offset(
            // // Pushed far to the left of the chart start
            // x = paddingStart - textLayoutResult.size.width - 50f,
            // y = y - (textLayoutResult.size.height)
            // )

            // )

            // }


            // --- 4. X-AXIS LABELS (Minimalist: Only 3 labels) ---
            val xSteps = 3
            for (i in 1..xSteps) {
                val dist = (distanceRange / xSteps) * i
                val x = getX(dist)

                val textLayoutResult = textMeasurer.measure(
                    text = "${"%.1f".format(dist)} km",
                    style = labelStyle
                )

                drawText(
                    textLayoutResult = textLayoutResult, topLeft = Offset(
                        x = x - (textLayoutResult.size.width + 70f),
                        // Pushed below the chart line
                        y = size.height - paddingBottom + 35f
                    )
                )
            }
        }
    }
}


private fun formatPace(p: Float): String {
    val min = p.toInt()
    val sec = ((p - min) * 60).toInt()
    return "%d:%02d".format(min, sec)
}

