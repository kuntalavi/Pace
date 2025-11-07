package com.rk.pace.presentation.screens.run.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
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
import com.rk.pace.common.ut.PathUt.toLatL
import com.rk.pace.domain.model.RunPathPoint

@Composable
fun RunMap(
    hasLocationPermission: Boolean,
    segments: List<List<RunPathPoint>>,
    onMapLoaded: () -> Unit
) {

    val cameraPositionState = rememberCameraPositionState()

    val latL = segments.toLatL()

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
        Polyline(points = latL, color = MaterialTheme.colorScheme.primary)
    }

}