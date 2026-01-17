package com.rk.pace.presentation.screens.active_run.components

import android.annotation.SuppressLint
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingPermission")
@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun RunMap(
    modifier: Modifier,
    segments: List<List<RunPathPoint>>,
    onMapLoaded: () -> Unit,
    isMapLoaded: Boolean
//    captureBitmap: Boolean,
//    onBitmapReady: (Bitmap) -> Unit
) {

    val cameraPositionState = rememberCameraPositionState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val fusedLocationProvider = LocationServices.getFusedLocationProviderClient(context)

    LaunchedEffect(key1 = isMapLoaded) {
        if (isMapLoaded) {
            val location = LatLng(0.0, 0.0)
            fusedLocationProvider.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                null
            ).addOnSuccessListener { location ->
                location?.let {
                    location.latitude = it.latitude
                    location.longitude = it.longitude
                }
            }.await()

            scope.launch {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            location.latitude,
                            location.longitude
                        ),
                        15f
                    )
                )
            }
        }
    }
//
//    LaunchedEffect(key1 = Unit) {
//        fusedLocationProvider.getCurrentLocation(
//            Priority.PRIORITY_LOW_POWER,
//            null
//        ).addOnSuccessListener { location ->
//            location?.let {
//                scope.launch {
//                    cameraPositionState.animate(
//                        update = CameraUpdateFactory.newLatLngZoom(
//                            LatLng(
//                                it.latitude,
//                                it.longitude
//                            ),
//                            15f
//                        )
//                    )
//                }
//            }
//        }
//    }

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
        onMapLoaded = onMapLoaded
    ) {

        MapEffect(key1 = segments) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        segments.lastOrNull()?.lastOrNull()?.lat ?: 0.0,
                        segments.lastOrNull()?.lastOrNull()?.long ?: 0.0
                    ),
                    18f
                )
            )
        }

//        MapEffect(key1 = captureBitmap) { map ->
//            if (captureBitmap) {
//                if (segments.isNotEmpty()) {
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
//                }
//            }
//        }

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