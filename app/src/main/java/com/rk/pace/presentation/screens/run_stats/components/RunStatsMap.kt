package com.rk.pace.presentation.screens.run_stats.components

import android.graphics.Bitmap
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.rk.pace.R
import com.rk.pace.common.ut.PathUt.toLatL
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.presentation.screens.active_run.components.getBounds
import kotlinx.coroutines.launch

@Composable
fun RunStatsMap(
    segments: List<List<RunPathPoint>>,
    captureBitmap: Boolean,
    onBitmapReady: (Bitmap) -> Unit
) {
    val cameraPositionState = rememberCameraPositionState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    fun moveToRun() {
        if (segments.isEmpty()) return
        val bounds = getBounds(segments)
        coroutineScope.launch {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    100
                )
            )
        }
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .size(200.dp),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false
        ),
        properties = MapProperties(
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                context,
                R.raw.map
            )
        ),
        onMapLoaded = {
            moveToRun()
        }
    ) {

        MapEffect(key1 = captureBitmap) { map ->
            if (captureBitmap) {
                if (segments.isNotEmpty()) {
//                    val bounds = getBounds(segments)
//                    map.animateCamera(
//                        CameraUpdateFactory.newLatLngBounds(
//                            bounds,
//                            100
//                        ),
//                        object: GoogleMap.CancelableCallback {
//                            override fun onCancel() {
//                            }
//                            override fun onFinish() {
//                                map.snapshot { bitmap ->
//                                    bitmap?.let {
//                                        onBitmapReady(it)
//                                    }
//                                }
//                            }
//                        }
//                    )
                    map.snapshot { bitmap ->
                        bitmap?.let {
                            onBitmapReady(it)
                        }
                    }
                }
            }
        }

        segments.forEach { segment ->
            val segment = segment.toLatL()
            if (segment.size > 1) {
                Polyline(
                    points = segment,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}