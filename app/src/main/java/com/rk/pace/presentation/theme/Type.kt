package com.rk.pace.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.rk.pace.R

val PJSANS = FontFamily(
    Font(
        resId = R.font.pj_sans,
        weight = FontWeight.Light
    ),
    Font(
        resId = R.font.pj_sans_r,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.pj_sans_m,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.pj_sans_sb,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.pj_sans_b,
        weight = FontWeight.Bold
    ),
)

val base = Typography()

val PaceTypography = Typography(

    displayLarge = base
        .displayLarge.copy(
            fontFamily = PJSANS
        ),

    displayMedium = base
        .displayMedium.copy(
            fontFamily = PJSANS
        ),

    displaySmall = base
        .displaySmall.copy(
            fontFamily = PJSANS
        ),

    headlineLarge = base
        .headlineLarge.copy(
            fontFamily = PJSANS
        ),

    headlineMedium = base
        .headlineMedium.copy(
            fontFamily = PJSANS
        ),

    headlineSmall = base
        .headlineSmall.copy(
            fontFamily = PJSANS
        ),

    titleLarge = base
        .titleLarge.copy(
            fontFamily = PJSANS
        ),

    titleMedium = base
        .titleMedium.copy(
            fontFamily = PJSANS
        ),

    titleSmall = base
        .titleSmall.copy(
            fontFamily = PJSANS
        ),

    bodyLarge = base
        .bodyLarge.copy(
            fontFamily = PJSANS
        ),

    bodyMedium = base
        .bodyMedium.copy(
            fontFamily = PJSANS
        ),

    bodySmall = base
        .bodySmall.copy(
            fontFamily = PJSANS
        ),

    labelLarge = base
        .labelLarge.copy(
            fontFamily = PJSANS
        ),

    labelMedium = base
        .labelMedium.copy(
            fontFamily = PJSANS
        ),

    labelSmall = base
        .labelSmall.copy(
            fontFamily = PJSANS
        ),

    )
