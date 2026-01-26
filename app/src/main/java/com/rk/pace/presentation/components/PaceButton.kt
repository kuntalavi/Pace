package com.rk.pace.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PaceButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    load: Boolean = false,
    onClick: () -> Unit,
    text: String,
    icon: ImageVector? = null,
    filled: Boolean = false
) {
    Button(
        modifier = modifier,
        onClick = {
            onClick()
        },
        enabled = enabled,
        border = BorderStroke(
            width = 1.dp,
            color = if (filled) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.primary
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (filled) MaterialTheme.colorScheme.onBackground
            else MaterialTheme.colorScheme.background
        ),
        shape = RoundedCornerShape(0.dp),
        contentPadding = PaddingValues(horizontal = 15.dp, vertical = 10.dp)
    ) {
        if (!load) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text.uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = if (filled) MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.onBackground
                    )
                )

                icon?.let {

                    Spacer(modifier = Modifier.width(10.dp))

                    Icon(
                        imageVector = icon,
                        tint = if (filled) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.primary,
                        contentDescription = ""
                    )

                }
            }
        } else {
            CircularProgressIndicator(
                color = if (filled) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.primary
            )
        }
    }
}