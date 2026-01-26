package com.rk.pace.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rk.pace.common.extension.formatDistance
import com.rk.pace.common.ut.TimestampUt.getBarDate
import com.rk.pace.domain.model.Run

@Composable
fun RunSummary(
    run: Run,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = com.rk.pace.theme.run,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onBackground
            )
            Spacer(
                modifier = Modifier
                    .width(10.dp)
            )
            Text(
                text = run.distanceMeters.formatDistance(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp
                )
            )
        }
        Text(
            text = getBarDate(
                run.timestamp
            ),
            letterSpacing = 1.sp
        )
    }
}