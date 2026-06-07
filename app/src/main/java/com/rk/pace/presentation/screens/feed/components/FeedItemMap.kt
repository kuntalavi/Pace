package com.rk.pace.presentation.screens.feed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rk.pace.presentation.theme.scheme
import com.rk.pace.presentation.ut.MapUt.buildStaticMapUrl

@Composable
fun FeedItemMap(
    path: List<String>
) {

    val context = LocalContext.current

    val mapWidth = 800
    val mapHeight = 600
    val color = scheme.primary

    val url = remember(path) {
        buildStaticMapUrl(
            path,
            mapWidth,
            mapHeight,
            color
        )
    }

    AsyncImage(
        model = ImageRequest
            .Builder(context)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(
                mapWidth.toFloat() / mapHeight.toFloat()
            )
            .background(
                scheme.onSurfaceVariant.copy(
                    alpha = .5f
                )
            )
    )
}