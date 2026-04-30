package com.rk.pace.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rk.pace.presentation.theme.Error
import com.rk.pace.presentation.theme.Success

/**
 * A beautiful, reusable stat display component for the Pace app.
 *
 * Displays a metric with optional icon, label, value, unit, and trend indicator.
 * Supports multiple layout styles and visual variants.
 *
 * @param value The primary value to display (e.g., "5.2", "42:15")
 * @param label The metric label (e.g., "Distance", "Pace", "Duration")
 * @param unit The unit of measurement (e.g., "km", "min/km", "min")
 * @param modifier Modifier for the component
 * @param icon Optional icon to display
 * @param trend Optional trend indicator ("+12%", "-5%", etc.)
 * @param trendPositive Whether the trend is positive (true) or negative (false)
 * @param subtitle Optional secondary text below the label
 * @param style Visual style variant of the stat item
 * @param emphasis Whether to emphasize this stat with accent colors
 */
@Composable
fun PaceStatItem(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    unit: String? = null,
    icon: ImageVector? = null,
    trend: String? = null,
    trendPositive: Boolean? = null,
    subtitle: String? = null,
    style: StatItemStyle = StatItemStyle.Card,
    emphasis: Boolean = false
) {

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "stat_alpha"
    )

    when (style) {
        StatItemStyle.Card -> StatItemCard(
            value = value,
            label = label,
            unit = unit,
            icon = icon,
            trend = trend,
            trendPositive = trendPositive,
            subtitle = subtitle,
            emphasis = emphasis,
            modifier = modifier.alpha(alpha)
        )

        StatItemStyle.Compact -> StatItemCompact(
            value = value,
            label = label,
            unit = unit,
            icon = icon,
            trend = trend,
            trendPositive = trendPositive,
            modifier = modifier.alpha(alpha)
        )

        StatItemStyle.Inline -> StatItemInline(
            value = value,
            label = label,
            unit = unit,
            modifier = modifier.alpha(alpha)
        )

        StatItemStyle.Hero -> StatItemHero(
            value = value,
            label = label,
            unit = unit,
            icon = icon,
            subtitle = subtitle,
            emphasis = emphasis,
            modifier = modifier.alpha(alpha)
        )
    }
}

/**
 * Card style - elevated container with generous spacing
 */
@Composable
private fun StatItemCard(
    value: String,
    label: String,
    unit: String?,
    icon: ImageVector?,
    trend: String?,
    trendPositive: Boolean?,
    subtitle: String?,
    emphasis: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (emphasis) {
        colorScheme.primaryContainer
    } else {
        colorScheme.surface
    }

    val contentColor = if (emphasis) {
        colorScheme.onPrimaryContainer
    } else {
        colorScheme.onSurface
    }

    Surface(
        modifier = modifier,
        color = backgroundColor,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = if (emphasis) 0.dp else 2.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = contentColor.copy(alpha = 0.7f)
                    )
                }

                Text(
                    text = label,
                    style = typography.labelLarge,
                    color = contentColor.copy(
                        alpha = 0.7f
                    ),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (trend != null && trendPositive != null) {
                    TrendIndicator(
                        trend = trend,
                        isPositive = trendPositive
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = value,
                    style = typography.displaySmall,
                    color = contentColor,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )

                if (unit != null) {
                    Text(
                        text = unit,
                        style = typography.titleMedium,
                        color = contentColor.copy(
                            alpha = 0.6f
                        ),
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = typography.bodySmall,
                    color = contentColor.copy(
                        alpha = 0.5f
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Compact style - minimal spacing, horizontal layout
 */
@Composable
private fun StatItemCompact(
    value: String,
    label: String,
    unit: String?,
    icon: ImageVector?,
    trend: String?,
    trendPositive: Boolean?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = label,
                style = typography.bodyMedium,
                color = colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = value,
                style = typography.titleLarge,
                color = colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.25).sp
            )

            if (unit != null) {
                Text(
                    text = unit,
                    style = typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }

            if (trend != null && trendPositive != null) {
                TrendIndicator(
                    trend = trend,
                    isPositive = trendPositive,
                    compact = true
                )
            }
        }
    }
}

/**
 * Inline style - single line, minimal formatting
 */
@Composable
private fun StatItemInline(
    value: String,
    label: String,
    unit: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = label,
            style = typography.bodySmall,
            color = colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 1.dp)
        )

        Text(
            text = value,
            style = typography.titleMedium,
            color = colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )

        if (unit != null) {
            Text(
                text = unit,
                style = typography.bodySmall,
                color = colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 1.dp)
            )
        }
    }
}

/**
 * Hero style - large, prominent display with gradient accent
 */
@Composable
private fun StatItemHero(
    value: String,
    label: String,
    unit: String?,
    icon: ImageVector?,
    subtitle: String?,
    emphasis: Boolean,
    modifier: Modifier = Modifier
) {
    val gradient = if (emphasis) {
        Brush.verticalGradient(
            colors = listOf(
                colorScheme.primaryContainer,
                colorScheme.primaryContainer.copy(alpha = 0.7f)
            )
        )
    } else {
        null
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (gradient != null) {
                    Modifier.background(gradient, MaterialTheme.shapes.large)
                } else {
                    Modifier
                }
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = if (emphasis) {
                            colorScheme.primary
                        } else {
                            colorScheme.onSurfaceVariant
                        }
                    )
                }

                Text(
                    text = label,
                    style = typography.titleMedium,
                    color = colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 2.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = value,
                    style = typography.displayLarge,
                    color = colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-1).sp
                )

                if (unit != null && value != "-") {
                    Text(
                        text = unit,
                        style = typography.headlineSmall,
                        color = colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Normal,
//                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

/**
 * Trend indicator badge
 */
@Composable
private fun TrendIndicator(
    trend: String,
    isPositive: Boolean,
    compact: Boolean = false
) {
    val trendColor = if (isPositive) {
        Success // Success green
    } else {
        Error // Error red
    }

    Surface(
        color = trendColor.copy(
            alpha = 0.12f
        ),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
    ) {
        Text(
            text = trend,
            style = if (compact) {
                typography.labelSmall
            } else {
                typography.labelMedium
            },
            color = trendColor,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(
                horizontal = if (compact) 6.dp else 8.dp,
                vertical = if (compact) 2.dp else 4.dp
            )
        )
    }
}

/**
 * Style variants for StatItem
 */
enum class StatItemStyle {
    /** Elevated card with generous spacing - best for grid layouts */
    Card,

    /** Horizontal compact layout - best for lists */
    Compact,

    /** Minimal inline display - best for dense layouts */
    Inline,

    /** Large prominent display - best for featured metrics */
    Hero
}