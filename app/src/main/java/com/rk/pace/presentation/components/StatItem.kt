package com.rk.pace.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.rk.pace.theme.Gray

@Composable
fun StatItem(
    title: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            letterSpacing = 3.sp,
            color = Gray,
            textAlign = TextAlign.Center
        )
        Text(
            text = value,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
//            letterSpacing = 3.sp,
            fontWeight = FontWeight.Bold,
//            color = Color.Black
        )
    }
}

