package com.rk.pace.presentation.screens.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rk.pace.theme.Black
import com.rk.pace.theme.arrow
import com.rk.pace.theme.back

@Composable
fun WeekNavigator(
    weekLabel: String,
    canGoForward: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onPrevious
        ) {
            Icon(
                imageVector = back,
                contentDescription = null
            )
        }

        Text(
            text = weekLabel,
            style = MaterialTheme.typography.titleSmall
        )

        IconButton(
            onClick = onNext,
            enabled = canGoForward
        ) {
            Icon(
                imageVector = arrow,
                contentDescription = null,
                tint = if (!canGoForward) MaterialTheme.colorScheme.background else Black
            )
        }
    }
}