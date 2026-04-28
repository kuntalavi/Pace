package com.rk.pace.presentation.screens.active_run.components

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.rk.pace.R
import com.rk.pace.common.extension.hasPreciseForegroundLocationPermission
import com.rk.pace.common.ut.PathUt.toLatL
import com.rk.pace.domain.model.RunPathPoint

@SuppressLint("MissingPermission")
@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun RunMap(
    modifier: Modifier,
    segments: List<List<RunPathPoint>>,
    location: RunPathPoint?,
    moveToUserTrigger: Int,
    isAct: Boolean,
    paused: Boolean,
    onMapLoadedCallback: () -> Unit
) {

    val cameraPositionState = rememberCameraPositionState()
    val context = LocalContext.current
    val darkTheme = isSystemInDarkTheme()

    var isInitialCenterDone by remember { mutableStateOf(false) }

    val uiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = false,
            rotationGesturesEnabled = false,
            myLocationButtonEnabled = false
        )
    }

    val mapProperties = remember(darkTheme) {
        MapProperties(
            isMyLocationEnabled = context.hasPreciseForegroundLocationPermission(),
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                context,
                if (darkTheme) R.raw.map_dark else R.raw.map
            )
        )
    }

    val mapSegments = remember(segments) {
        segments
            .filter { it.size > 1 }
            .map { it.toLatL() }
    }


    LaunchedEffect(key1 = location) {
        if (location != null && !isInitialCenterDone) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        location.lat,
                        location.long
                    ),
                    18f
                )
            )
            isInitialCenterDone = true
        }
    }

    LaunchedEffect(key1 = moveToUserTrigger) {
        location?.let {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        location.lat,
                        location.long
                    ),
                    18f
                )
            )
        }
    }

    LaunchedEffect(key1 = location) {
        if (isAct && !paused) {
            location?.let {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLng(
                        LatLng(
                            location.lat,
                            location.long
                        )
                    )
                )
            }
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        uiSettings = uiSettings,
        properties = mapProperties,
        onMapLoaded = onMapLoadedCallback
    ) {
        mapSegments.forEach { segment ->
            Polyline(
                points = segment,
                color = colorScheme.primary,
                width = 15f,
                jointType = JointType.ROUND
            )
        }
    }
}