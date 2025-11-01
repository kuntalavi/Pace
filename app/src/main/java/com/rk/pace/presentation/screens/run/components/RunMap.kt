package com.rk.pace.presentation.screens.run.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline

@Composable
fun RunMap(
    hasLocationPermission: Boolean,
    cameraPositionState: CameraPositionState,
    segments: List<List<LatLng>>
) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false
        ),
        properties = MapProperties(
            isMyLocationEnabled = hasLocationPermission
        )
    ) {
        segments.forEach { segment ->
            Polyline(points = segment)
        }
    }

}