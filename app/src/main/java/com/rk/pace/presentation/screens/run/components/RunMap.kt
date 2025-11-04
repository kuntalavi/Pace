package com.rk.pace.presentation.screens.run.components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.rk.pace.presentation.theme.Black

@Composable
fun RunMap(
    hasLocationPermission: Boolean,
    path: List<LatLng>,
    onMapLoaded: () -> Unit
) {

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(path) {
        Log.d(
            "MapScreen",
            "PolyLine Points: ${path.lastOrNull()?.latitude} ${path.lastOrNull()?.longitude}"
        )
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    path.lastOrNull()?.latitude ?: 28.7041,
                    path.lastOrNull()?.longitude ?: 77.1025
                ),
                15f
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
        Polyline(
            points = path,
            color = Black
        )
//        segments.forEach { segment ->
//            Polyline(points = segment)
//        }
    }

}