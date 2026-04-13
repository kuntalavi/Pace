package com.rk.pace.presentation.charts

import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.rk.pace.domain.model.GoalProgress
import com.rk.pace.theme.Gray

@Composable
fun GoalPieChart(
    progress: GoalProgress,
    modifier: Modifier = Modifier,
    filledColor: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = Gray.copy(alpha = .1f),
    completedColor: Color = MaterialTheme.colorScheme.primary
) {
    val activeColor = if (progress.isCompleted) completedColor else filledColor
    val animatedFraction by animateFloatAsState(
        targetValue = progress.fraction,
        animationSpec = tween(durationMillis = 800, easing = EaseOutCubic),
        label = ""
    )
    val sweepAngle = 360f * animatedFraction

    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        val strokeWidth = 40.dp.toPx()
        val stroke = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Square
        )
        val inset = strokeWidth / 2
        val arcSize = Size(size.width - inset * 2, size.height - inset * 2)
        val topLeft = Offset(inset, inset)

        // TRACK
        drawArc(
            color = trackColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = stroke
        )
        // PROGRESS
        if (animatedFraction > 0f) {
            drawArc(
                color = activeColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = stroke
            )
        }

    }
}