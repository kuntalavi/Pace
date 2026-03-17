package com.rk.pace.presentation.screens.active_run.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
        GpsStrength.MODERATE -> Amber
        GpsStrength.STRONG -> Green
        else -> Red
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        Text(
            text = "GPS",
            modifier = Modifier.offset(y = 4.dp),
            style = MaterialTheme.typography.titleMedium.copy(
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        )

        Spacer(
            modifier = Modifier
                .width(2.dp)
        )

        SignalBar(
            active = strength != GpsStrength.NONE,
            height = 6.dp,
            color = color
        )

        SignalBar(
            active = strength == GpsStrength.MODERATE || strength == GpsStrength.STRONG,
            height = 10.dp,
            color = color
        )

        SignalBar(
            active = strength == GpsStrength.STRONG,
            height = 14.dp,
            color = color
        )
    }

}

@Composable
fun SignalBar(
    modifier: Modifier = Modifier,
    active: Boolean,
    height: Dp,
    color: Color
) {
    Box(
        modifier = modifier
            .width(4.dp)
            .height(height)
            .background(
                color = if (active) color else Gray.copy(alpha = .5f),
                shape = RoundedCornerShape(0.dp)
            )
    )
}