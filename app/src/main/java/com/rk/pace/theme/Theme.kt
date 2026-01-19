package com.rk.pace.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFc6c6c6),
    secondary = Color(0xFFc6c6c6),
    tertiary = Color(0xFFc6c6c6),
    error = Color(0xFFffb4ab),

    onPrimary = Color(0xFF303030),
    onSecondary = Color(0xFF303030),
    onTertiary = Color(0xFF303030),
    onError = Color(0xFF690005),

    primaryContainer = Black,
    secondaryContainer = Color(0xFF474747),
    tertiaryContainer = Black,
    errorContainer = Color(0xFF93000a),

    onPrimaryContainer = Color(0xFF757575),
    onSecondaryContainer = Color(0xFFb5b5b5),
    onTertiaryContainer = Color(0xFF757575),
    onErrorContainer = Color(0xFFffdad6),

    surface = Color(0xFF131313),
    onSurface = Color(0xFFe2e2e2),
    inverseSurface = Color(0xFFe2e2e2),
    inverseOnSurface = Color(0xFF303030),
    inversePrimary = Color(0xFF5e5e5e),
    scrim = Black,
)

private val LightColorScheme = lightColorScheme(
    primary = Black,
    secondary = Color(0xFF5e5e5e),
    tertiary = Black,
    error = Color(0xFFba1a1a),

    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onError = White,

    primaryContainer = Color(0xFF1b1b1b),
    secondaryContainer = Color(0xFFe2e2e2),
    tertiaryContainer = Color(0xFF1b1b1b),
    errorContainer = Color(0xFFffdad6),

    onPrimaryContainer = Color(0xFF848484),
    onSecondaryContainer = Color(0xFF646464),
    onTertiaryContainer = Color(0xFF848484),
    onErrorContainer = Color(0xFF93000a),

    surface = Color(0xFFf9f9f9),
    onSurface = Color(0xFF1b1b1b),
    inverseSurface = Color(0xFF303030),
    inverseOnSurface = Color(0xFFf1f1f1),
    inversePrimary = Color(0xFFc6c6c6),
    scrim = Black,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun PaceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }

    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}