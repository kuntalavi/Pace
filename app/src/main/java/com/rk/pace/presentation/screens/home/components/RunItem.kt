package com.rk.pace.presentation.screens.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rk.pace.BuildConfig
import com.rk.pace.domain.model.Run

@Composable
fun RunItem(
    run: Run,
    eRunPath: String
) {

    val runMap = "https://maps.googleapis.com/maps/api/staticmap" +
//            "?center=${run.latitude},${run.longitude}" +
            "&zoom=15" +
            "&size=600x300" +
            "&maptype=roadmap" +
            "&path=weight:4|enc:$eRunPath" +
//            "&markers=color:red%7Clabel:R%7C${run.latitude},${run.longitude}" +
            "&key=${BuildConfig.GOOGLE_MAPS_API_KEY}"

    Card {
        AsyncImage(
            model = runMap,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
    }
}