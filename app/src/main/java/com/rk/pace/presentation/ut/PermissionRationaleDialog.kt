package com.rk.pace.presentation.ut

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rk.pace.presentation.components.ButtonVariant
import com.rk.pace.presentation.components.PaceButton

@Composable
fun PermissionRationaleDialog(
    title: String = "Grant Permission",
    text: String,
    confirmLabel: String = "Allow",
    dismissLabel: String? = "Not now",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(0.dp),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Text(
                text = text
            )
        },
        confirmButton = {
            PaceButton(
                onClick = onConfirm,
                text = confirmLabel,
                variant = ButtonVariant.Filled
            )
        },
        dismissButton = dismissLabel?.let {
            {
                PaceButton(
                    onClick = onDismiss,
                    text = it
                )
            }
        }
    )
}