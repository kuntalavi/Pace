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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.rk.pace.domain.model.PacePoint
import com.rk.pace.presentation.theme.Black

@Composable
fun PaceChart(
    data: List<PacePoint>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary
) {
    val paddingStart = 60f
    val paddingEnd = 20f
    val paddingTop = 20f
    val paddingBottom = 50f

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Text(
            text = "PACE",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(
                    horizontal = 10.dp
                )
        ) {

            if (data.isEmpty()) return@Canvas

            val width = size.width - paddingStart - paddingEnd
            val height = size.height - paddingTop - paddingBottom

            val minPace = data.minOf { it.paceMinPerKm }
            val maxPace = data.maxOf { it.paceMinPerKm }
            val paceRange = maxPace - minPace

            val maxDistance = data.maxOf { it.distanceKm }
            val distanceRange = if (maxDistance == 0f) 1f else maxDistance

            fun getX(distance: Float) =
                paddingStart + (distance / distanceRange) * width

            fun getY(pace: Float): Float {
                val clamped = pace.coerceIn(minPace, maxPace)
                val ratio = (clamped - minPace) / paceRange
                return paddingTop + (1 - ratio) * height
            }

            val points = data.map {
                Offset(
                    x = getX(it.distanceKm),
                    y = getY(it.paceMinPerKm)
                )
            }

            val linePath = Path().apply {
                moveTo(points.first().x, points.first().y)

                for (i in 1 until points.size) {
                    val prev = points[i - 1]
                    val curr = points[i]
                    val midX = (prev.x + curr.x) / 2

                    cubicTo(
                        midX, prev.y,
                        midX, curr.y,
                        curr.x, curr.y
                    )
                }
            }

            val fillPath = Path().apply {
                addPath(linePath)
                lineTo(points.last().x, size.height - paddingBottom)
                lineTo(points.first().x, size.height - paddingBottom)
                close()
            }

            /*
            * AREA UNDER THE CURVE
            * */
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        lineColor.copy(
                            alpha = 0.5f
                        ),
                        lineColor.copy(
                            alpha = 0.3f
                        ),
                        Color.Transparent
                    )
                )
            )

            val avgPace = data.map { it.paceMinPerKm }.average().toFloat()
            val avgY = getY(avgPace)

            /*
            * AVG PACE L
            * */
            drawLine(
                color = Black.copy(alpha = 0.6f),
                start = Offset(paddingStart, avgY),
                end = Offset(size.width - paddingEnd, avgY),
                strokeWidth = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(
                    floatArrayOf(10f, 20f)
                )
            )

            /*
            * CURVE
            * */
            drawPath(
                path = linePath,
                color = lineColor,
                style = Stroke(
                    width = 1.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )

            /*
            * AXIS LABELS
            * */
            repeat(4) { i ->
                val dist = (distanceRange / 4) * (i + 1)
                val x = getX(dist)

                drawContext.canvas.nativeCanvas.drawText(
                    "%.1f".format(dist),
                    x,
                    size.height - 10f,
                    android.graphics.Paint().apply {
                        textSize = 14f
                        color = android.graphics.Color.GRAY
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }

            repeat(4) { i ->
                val pace = minPace + (paceRange / 4) * (i + 1)
                val y = getY(pace)

                drawContext.canvas.nativeCanvas.drawText(
                    formatPace(pace),
                    paddingStart - 10f,
                    y + 5f,
                    android.graphics.Paint().apply {
                        textSize = 14f
                        color = android.graphics.Color.GRAY
                        textAlign = android.graphics.Paint.Align.LEFT
                    }
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