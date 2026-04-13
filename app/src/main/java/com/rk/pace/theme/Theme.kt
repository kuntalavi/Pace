package com.rk.pace.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    /*
    * PRIMARY - MAIN BRAND TEAL
    * */
    primary = Teal200, // PRIMARY BUTTONS. FAB, ACTIVE NAV
    onPrimary = Teal900, // TEXT/ICON ON PRIMARY
    primaryContainer = Teal600, //  CHIP BG, SELECTED CARD BG
    onPrimaryContainer = Teal50, // TEXT INSIDE PRIMARY CONTAINER

    /*
    * SECONDARY - AMBER FOR ENERGY/MILESTONES
    * */
    secondary = Amber100, // PR BADGE, MILESTONE RING
    onSecondary = Amber900, // TEXT/ICON ON SECONDARY
    secondaryContainer = Amber600, // MILESTONE CARD BG
    onSecondaryContainer = Amber50, // TEXT INSIDE SECONDARY CONTAINER

    /*
    * TERTIARY - A SOFTER TEAL FOR SUPPORTING ACCENTS
    * */
    tertiary = Teal100, // PACE GRAPH LINE, SPARKLINE
    onTertiary = Teal900, // TEXT/ICON ON TERTIARY
    tertiaryContainer = Teal800, // SUBTLE HIGHLIGHT SURFACE
    onTertiaryContainer = Teal100, // TEXT INSIDE TERTIARY CONTAINER

    /*
    * BACKGROUND AND SURFACE
    * */
    background = Teal900, // SCREEN BG
    onBackground = Teal50, // PRIMARY TEXT ON BG

    /*
    * SURFACE
    * */
    surface = Teal800, // CARD, BOTTOM SHEET. DIALOG BG
    onSurface = Gray50, // TEXT ON SURFACE
    surfaceVariant = Teal600, // STAT CHIPS, TAG BG
    onSurfaceVariant = Teal100, // SECONDARY TEXT ON SURFACE VARIANT

    /*
    * OUTLINE
    * */
    outline = Teal400, // DIVIDERS, INPUT BORDERS
    outlineVariant = Teal600, // SUBTLE DIVIDERS

    /*
    * ERROR
    * */
    error = Color(0xFFF09595),
    onError = Color(0xFF501313),
    errorContainer = Color(0xFFA32D2D),
    onErrorContainer = Color(0xFFFCEBEB),

    /*
    * INVERSE (SNACKBARS. TOOLTIPS)
    * */
    inverseSurface = Gray50,
    inverseOnSurface = Gray900,
    inversePrimary = Teal600,

    /*
    * SCRIM (MODAL OVERLAY)
    * */
    scrim = Black,
)

private val LightColorScheme = lightColorScheme(
    /*
    * PRIMARY - MAIN BRAND TEAL
    * */
    primary = Teal600, // PRIMARY BUTTONS. FAB, ACTIVE NAV
    onPrimary = White, // TEXT/ICON ON PRIMARY
    primaryContainer = Teal50, //  CHIP BG, SELECTED CARD BG
    onPrimaryContainer = Teal800, // TEXT INSIDE PRIMARY CONTAINER

    /*
    * SECONDARY - AMBER FOR ENERGY/MILESTONES
    * */
    secondary = Amber400, // PR BADGE, MILESTONE RING
    onSecondary = White, // TEXT/ICON ON SECONDARY
    secondaryContainer = Amber50, // MILESTONE CARD BG
    onSecondaryContainer = Amber900, // TEXT INSIDE SECONDARY CONTAINER

    /*
    * TERTIARY - A SOFTER TEAL FOR SUPPORTING ACCENTS
    * */
    tertiary = Teal200, // PACE GRAPH LINE, SPARKLINE
    onTertiary = Teal900, // TEXT/ICON ON TERTIARY
    tertiaryContainer = Teal50, // SUBTLE HIGHLIGHT SURFACE
    onTertiaryContainer = Teal600, // TEXT INSIDE TERTIARY CONTAINER

    /*
    * BACKGROUND AND SURFACE
    * */
    background = Gray50, // SCREEN BG
    onBackground = Gray900, // PRIMARY TEXT ON BG

    /*
    * SURFACE
    * */
    surface = White, // CARD, BOTTOM SHEET. DIALOG BG
    onSurface = Gray900, // TEXT ON SURFACE
    surfaceVariant = Teal50, // STAT CHIPS, TAG BG
    onSurfaceVariant = Gray600, // SECONDARY TEXT ON SURFACE VARIANT

    /*
    * OUTLINE
    * */
    outline = Gray200, // DIVIDERS, INPUT BORDERS
    outlineVariant = Gray100, // SUBTLE DIVIDERS

    /*
    * ERROR
    * */
    error = Error,
    onError = White,
    errorContainer = Color(0xFFFCEBEB),
    onErrorContainer = Color(0xFF791F1F),

    /*
    * INVERSE (SNACKBARS. TOOLTIPS)
    * */
    inverseSurface = Gray900,
    inverseOnSurface = Gray50,
    inversePrimary = Teal200,

    /*
    * SCRIM (MODAL OVERLAY)
    * */
    scrim = Black,
)

@Composable
fun PaceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PaceTypography,
        content = content
    )
}