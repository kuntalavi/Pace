package com.rk.pace.presentation.screens.active_run.components

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.rk.pace.R
import com.rk.pace.common.extension.hasLocationPermission
import com.rk.pace.common.ut.PathUt.toLatL
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.theme.Red

@SuppressLint("MissingPermission")
@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun RunMap(
    modifier: Modifier,
    segments: List<List<RunPathPoint>>,
    currentLocation: RunPathPoint,
    isAct: Boolean,
    paused: Boolean,
    bottomPaddingDp: Dp,
    onMapLoadedCallback: () -> Unit
) {

    val density = LocalDensity.current
    val bottomPaddingPx = with(density) { bottomPaddingDp.toPx() }.toInt()

    val cameraPositionState = rememberCameraPositionState()
    val context = LocalContext.current

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false
        ),
        properties = MapProperties(
            isMyLocationEnabled = context.hasLocationPermission(),
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                context,
                R.raw.map
            )
        ),
        onMapLoaded = {
            onMapLoadedCallback()
        }
    ) {

        // bottom sheet and top p
        MapEffect(key1 = bottomPaddingPx) { map ->
            map.setPadding(
                0,
                100,
                0,
                bottomPaddingPx
            )
        }

        // follow user location when !active
        MapEffect(
            key1 = currentLocation,
            key2 = isAct,
            key3 = paused
        ) {
            if (!isAct || paused) {
                if (currentLocation.lat != 0.0) {
                    cameraPositionState.animate(
                        update = CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                currentLocation.lat,
                                currentLocation.long
                            ),
                            18f
                        )
                    )
                }
            }
        }

        // follow active run path when active
        MapEffect(key1 = segments) {
            if (isAct && !paused) {
                val lastPoint = segments.lastOrNull()?.lastOrNull()
                if (lastPoint != null) {
                    cameraPositionState.animate(
                        update = CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                lastPoint.lat,
                                lastPoint.long
                            ),
                            18f
                        )
                    )
                }
            }
        }

        // p
        segments.forEach { segment ->
            val segment = segment.toLatL()
            if (segment.size > 1) {
                Polyline(
                    points = segment,
                    color = Red
                )
            }
        }
    }
}

fun getBounds(
    segments: List<List<RunPathPoint>>
): LatLngBounds {
    val boundsB = LatLngBounds.Builder()
    segments.forEach { segment ->
        segment.forEach { point ->
            boundsB.include(
                LatLng(
                    point.lat,
                    point.long
                )
            )
        }
    }
    return boundsB.build()
}