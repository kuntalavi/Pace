package com.rk.pace.presentation.screens.feed.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.rk.pace.common.ut.MapUt.buildStaticMapUrl
import kotlin.math.roundToInt

@Composable
fun FeedItemMap(
    path: List<String>
) {

    val mapWidth = 800
    val mapHeight = 600
    val color = colorScheme.primary

    val hex = String.format(
        "%02X%02X%02X",
        (color.red * 255).roundToInt(),
        (color.green * 255).roundToInt(),
        (color.blue * 255).roundToInt()
    )

    val url = remember(path) {
        buildStaticMapUrl(
            path,
            mapWidth,
            mapHeight,
            hex
        )
    }

    AsyncImage(
        model = url,
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(mapWidth.toFloat() / mapHeight.toFloat())
    )
}