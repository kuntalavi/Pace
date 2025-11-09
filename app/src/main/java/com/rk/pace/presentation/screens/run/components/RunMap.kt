package com.rk.pace.presentation.screens.run.components

import android.graphics.Bitmap
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.rk.pace.common.ut.PathUt.toLatL
import com.rk.pace.domain.model.RunPathPoint

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun RunMap(
    hasLocationPermission: Boolean,
    segments: List<List<RunPathPoint>>,
    onMapLoaded: () -> Unit,
    captureBitmap: Boolean,
    onBitmapReady: (Bitmap) -> Unit
) {

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(segments) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    segments.lastOrNull()?.lastOrNull()?.lat ?: 28.7041,
                    segments.lastOrNull()?.lastOrNull()?.l ?: 77.1025
                ),
                18f
            )
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false
        ),
        properties = MapProperties(
            isMyLocationEnabled = hasLocationPermission
        ),
        onMapLoaded = onMapLoaded
    ) {

        MapEffect(key1 = captureBitmap) { map ->
            if (captureBitmap) {


                zoomToBounds(map, segments, onBitmapReady)

//                map.snapshot { bitmap ->
//                    bitmap?.let {
//                        onBitmapReady(it)
//                    }
//                }
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

fun snapshotMap(
    map: GoogleMap,
    onBitmapReady: (Bitmap) -> Unit
) {
    map.snapshot { bitmap ->
        bitmap?.let {
            onBitmapReady(it)
        }
    }
}

fun zoomToBounds(
    map: GoogleMap,
    segments: List<List<RunPathPoint>>,
    onBitmapReady: (Bitmap) -> Unit
) {
    val boundsB = LatLngBounds.Builder()
    segments.forEach { segment ->
        segment.forEach { point ->
            boundsB.include(
                LatLng(
                    point.lat,
                    point.l
                )
            )
        }
    }
    val bounds = boundsB.build()

    map.animateCamera(
        CameraUpdateFactory.newLatLngBounds(bounds, 100),
        1000,
        object : GoogleMap.CancelableCallback {
            override fun onFinish() {
                snapshotMap(map, onBitmapReady = onBitmapReady)
            }

            override fun onCancel() {
            }

        }
    )

}