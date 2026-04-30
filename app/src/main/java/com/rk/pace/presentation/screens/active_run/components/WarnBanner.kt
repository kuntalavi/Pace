package com.rk.pace.presentation.screens.active_run.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rk.pace.presentation.theme.close

@Composable
fun WarnBanner(
    message: String?
) {

    if (message == null) return

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorScheme.errorContainer)
            .padding(
                vertical = 12.dp,
                horizontal = 16.dp
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = close,
                contentDescription = null,
                tint = colorScheme.onErrorContainer,
                modifier = Modifier.padding(
                    end = 12.dp
                )
            )
            Text(
                text = message,
                color = colorScheme.onErrorContainer,
                style = typography.bodyMedium
            )
        }
    }
}