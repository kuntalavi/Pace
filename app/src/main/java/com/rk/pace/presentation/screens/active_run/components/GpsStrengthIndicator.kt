package com.rk.pace.presentation.screens.active_run.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.rk.pace.domain.tracking.GpsStrength
import com.rk.pace.theme.close

@Composable
fun GpsStrengthIndicator(
    modifier: Modifier = Modifier,
    strength: GpsStrength
) {

    val level = when (strength) {
        GpsStrength.NONE -> 0
        GpsStrength.WEAK -> 1
        GpsStrength.MODERATE -> 2
        GpsStrength.STRONG -> 3
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (level == 0) {
                Icon(
                    imageVector = close,
                    contentDescription = null,
                    tint = colorScheme.error
                )
            } else {
                repeat(3) { i ->
                    SignalBar(
                        active = i < level
                    )
                }
            }
        }

        Text(
            text = "GPS",
            style = MaterialTheme.typography.labelSmall
        )
    }

}

@Composable
fun SignalBar(
    modifier: Modifier = Modifier,
    active: Boolean
) {
    Box(
        modifier = modifier
            .width(4.dp)
            .height(10.dp)
            .background(
                color = if (active) colorScheme.onSurface else colorScheme.onSurface.copy(
                    alpha = .3f
                ),
                shape = RectangleShape
            )
    )
}