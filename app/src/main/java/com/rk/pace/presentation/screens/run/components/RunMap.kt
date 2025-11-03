package com.rk.pace.presentation.screens.run.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.rk.pace.domain.model.RunPathPoint

@Composable
fun RunMap(
    hasLocationPermission: Boolean,
    cameraPositionState: CameraPositionState,
    segments: List<RunPathPoint>,
    onMapLoaded: () -> Unit
) {
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
//        segments.forEach { segment ->
//            Polyline(points = segment)
//        }
    }

}