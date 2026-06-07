package com.rk.pace.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PermissionRationaleDialog(
    title: String = "Grant Permission",
    body: String,
    confirmLabel: String = "Allow",
    dismissLabel: String? = "Not now",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = shapes.extraSmall,
        title = {
            Text(
                text = title,
                style = typography.titleMedium
            )
        },
        text = {
            Text(
                text = body,
                style = typography.bodyMedium
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
                    text = it,
                    variant = ButtonVariant.Tonal
                )
            }
        }
    )
}