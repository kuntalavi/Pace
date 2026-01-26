package com.rk.pace.presentation.screens.active_run.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rk.pace.domain.tracking.GpsStrength
import com.rk.pace.theme.Amber
import com.rk.pace.theme.Gray
import com.rk.pace.theme.Green
import com.rk.pace.theme.Red

@Composable
fun GpsStrengthIndicator(
    modifier: Modifier = Modifier,
    strength: GpsStrength
) {

    val color = when (strength) {
        GpsStrength.NONE -> Amber
        GpsStrength.WEAK -> Red
        GpsStrength.MODERATE -> Amber
        GpsStrength.STRONG -> Green
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = "GPS",
            style = MaterialTheme.typography.titleMedium.copy(
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(
            modifier = Modifier
                .width(4.dp)
        )

        SignalBar(
            active = strength != GpsStrength.NONE,
            height = 8.dp,
            activeC = color
        )
        SignalBar(
            active = strength == GpsStrength.MODERATE || strength == GpsStrength.STRONG,
            height = 12.dp,
            activeC = color
        )
        SignalBar(
            active = strength == GpsStrength.STRONG,
            height = 16.dp,
            activeC = color
        )
    }
}

@Composable
fun SignalBar(
    modifier: Modifier = Modifier,
    active: Boolean,
    height: Dp,
    activeC: Color
) {
    Box(
        modifier = modifier
            .width(4.dp)
            .height(height)
            .background(
                color = if (active) activeC else Gray.copy(alpha = .5f),
                shape = RoundedCornerShape(0.dp)
            )
    )
}