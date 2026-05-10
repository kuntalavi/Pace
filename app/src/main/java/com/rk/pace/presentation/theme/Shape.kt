package com.rk.pace.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val shape = RoundedCornerShape(4.dp)

data class Space(
    val xSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val xLarge: Dp = 32.dp
)

data class Elevation(
    val lev0: Dp = 0.dp,
    val lev1: Dp = 2.dp,
    val lev2: Dp = 8.dp,
    val lev3: Dp = 16.dp
)

val LocalSpace = compositionLocalOf { Space() }
val LocalElevation = compositionLocalOf { Elevation() }

val MaterialTheme.space: Space
    @Composable
    @ReadOnlyComposable
    get() = LocalSpace.current

val MaterialTheme.elevation: Elevation
    @Composable
    @ReadOnlyComposable
    get() = LocalElevation.current