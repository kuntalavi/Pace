package com.rk.pace.presentation.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.rememberAsyncImagePainter
import com.rk.pace.domain.model.Run

@Composable
fun RunItem(
    run: Run
) {

    Card {
        RunImage(imageURI = run.bitmapURI)
    }
}

@Composable
fun RunImage(
    imageURI: String?
) {
    if (imageURI == null) return
    Image(
        painter = rememberAsyncImagePainter(model = imageURI.toUri()),
        contentDescription = "",
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        contentScale = ContentScale.Fit
    )
}