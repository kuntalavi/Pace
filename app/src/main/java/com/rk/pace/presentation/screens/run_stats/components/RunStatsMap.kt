package com.rk.pace.presentation.screens.run_stats.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.rk.pace.R
import com.rk.pace.common.ut.PathUt.toLatL
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.presentation.screens.active_run.components.getBounds
import com.rk.pace.theme.Red
import kotlinx.coroutines.launch

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun RunStatsMap(
    modifier: Modifier = Modifier,
    segments: List<List<RunPathPoint>>,
    bottomPaddingDp: Dp,
    onMapLoadedCallback: () -> Unit
) {

    val density = LocalDensity.current
    val bottomPaddingPx = with(density) { bottomPaddingDp.toPx() }.toInt()

    val cameraPositionState = rememberCameraPositionState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    fun moveToRun() {
        val hasPoints = segments.any {
            it.isNotEmpty()
        }
        if (!hasPoints) return
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
        modifier = modifier,
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
            onMapLoadedCallback()
            moveToRun()
        }
    ) {

        MapEffect(bottomPaddingPx) { map ->
            map.setPadding(
                0,
                0,
                0,
                bottomPaddingPx
            )
        }

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