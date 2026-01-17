package com.rk.pace.presentation.screens.feed.components

import android.util.Log
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.rk.pace.common.ut.MapUt.buildStaticMapUrl

@Composable
fun FeedItemMap(
    path: List<String>
) {

    val mapWidth = 600
    val mapHeight = 300

    val url = remember(path) {
        buildStaticMapUrl(path, mapWidth, mapHeight)
    }

    AsyncImage(
        model = url,
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
        onError = { Log.e("MAPBOX", "Error: ${it.result.throwable}") },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(mapWidth.toFloat() / mapHeight.toFloat())
    )
}