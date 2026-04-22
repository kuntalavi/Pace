package com.rk.pace.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rk.pace.common.Constants.shape

enum class ButtonVariant {
    Filled,
    Outlined,
    Tonal
}

@Composable
fun PaceButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    load: Boolean = false,
    onClick: () -> Unit,
    text: String,
    icon: ImageVector? = null,
    variant: ButtonVariant = ButtonVariant.Outlined
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.97f else 1f,
        label = ""
    )

    val (containerColor, contentColor, borderColor) = when (variant) {
        ButtonVariant.Filled -> Triple(
            colorScheme.primary,
            colorScheme.onPrimary,
            null
        )

        ButtonVariant.Outlined -> Triple(
            colorScheme.surface,
            colorScheme.primary,
            colorScheme.primary
        )

        ButtonVariant.Tonal -> Triple(
            Color.Transparent,
            colorScheme.primary,
            null
        )
    }

    val style = if (
        text.all { it.isUpperCase() || it == ' ' }
    ) {
        typography.labelLarge.copy(
            letterSpacing = 2.sp
        )
    } else typography.labelLarge

    Button(
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        onClick = onClick,
        enabled = enabled && !load,
        interactionSource = interactionSource,
        border = borderColor?.let {
            BorderStroke(
                width = 1.dp,
                color = it
            )
        },
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(
                alpha = 0.38f
            ),
            disabledContentColor = contentColor.copy(
                alpha = 0.38f
            )
        ),
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 10.dp
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (variant == ButtonVariant.Filled) 2.dp else 0.dp,
            pressedElevation = if (variant == ButtonVariant.Filled) 4.dp else 0.dp,
            disabledElevation = 0.dp
        )
    ) {
        if (load) {
            CircularProgressIndicator(
                modifier = Modifier.size(
                    20.dp
                ),
                color = contentColor,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        tint = contentColor,
                        contentDescription = null,
                        modifier = Modifier.size(
                            18.dp
                        )
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )
                }

                Text(
                    text = text,
                    style = style,
                    color = contentColor
                )
            }
        }
    }
}