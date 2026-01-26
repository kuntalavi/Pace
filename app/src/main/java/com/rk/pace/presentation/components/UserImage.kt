package com.rk.pace.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rk.pace.theme.user

sealed class UserImageSize(
    val size: Dp,
    val borderWidth: Dp
) {
    data object Small : UserImageSize(32.dp, 1.dp)
    data object Medium : UserImageSize(48.dp, 1.5.dp)
    data object Large : UserImageSize(96.dp, 2.dp)
    data object XLarge : UserImageSize(140.dp, 3.dp)
}

@Composable
fun UserImage(
    imageUrl: String?,
    size: UserImageSize,
    modifier: Modifier = Modifier,
    showBorder: Boolean = false,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    onClick: (() -> Unit)? = null
) {
    val shape = CircleShape

    Box(
        modifier = modifier
            .size(size.size)
            .clip(shape)
            .then(
                if (showBorder) {
                    Modifier.border(
                        width = size.borderWidth,
                        color = borderColor,
                        shape = shape
                    )
                } else Modifier
            )
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(enabled = onClick != null) {
                onClick?.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                imageVector = user,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(size.size / 2)
            )
        }
    }
}