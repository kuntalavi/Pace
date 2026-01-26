package com.rk.pace.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.rk.pace.R

val Oswald = FontFamily(
    Font(R.font.oswald, FontWeight.Normal)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Oswald,
//        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
//    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = Oswald,
//        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
//        lineHeight = 28.sp,
//        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Oswald,
//        fontWeight = FontWeight.Medium,
//        fontSize = 11.sp,
//        lineHeight = 16.sp,
//        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Oswald
    ),
    titleSmall = TextStyle(
        fontFamily = Oswald
    ),
    labelLarge = TextStyle(
        fontFamily = Oswald
    ),
    labelMedium = TextStyle(
        fontFamily = Oswald
    ),
    bodyMedium = TextStyle(
        fontFamily = Oswald
    ),
    bodySmall = TextStyle(
        fontFamily = Oswald
    ),
    headlineLarge = TextStyle(
        fontFamily = Oswald
    ),
    headlineMedium = TextStyle(
        fontFamily = Oswald
    ),
)