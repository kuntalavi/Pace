package com.rk.pace.presentation.run

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.rk.pace.presentation.run.components.RunMap
import com.rk.pace.presentation.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunScreen(
    viewModel: RunViewModel = hiltViewModel(),
    goBack: () -> Unit
) {

    val context = LocalContext.current

    val cameraPositionState = rememberCameraPositionState()
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }


    val isheaderExpanded = viewModel.isheaderExpanded

    val headerHeight by animateDpAsState(
        targetValue = if (isheaderExpanded) 200.dp else 100.dp,
        label = "HeaderHeight"
    )

    val requestLocationPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            moveToUserLocation(fusedLocationClient, cameraPositionState)
        } else {
            goBack()
        }
    }

    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.setLocationPermission(true)
                moveToUserLocation(fusedLocationClient, cameraPositionState)
            }

            else -> {
                viewModel.showRationale()
            }
        }
    }

    if (viewModel.showRationale) {
        Dialog(
            onDismissRequest = {}) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Location permission is needed to show your current location on the map."
                )
                Button(
                    onClick = {
                        viewModel.onRationaleDismissed()
                        requestLocationPermission.launch(
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    }
                ) {
                    Text(text = "Allow")
                }
                Button(
                    onClick = {
                        viewModel.onRationaleDismissed()
                        goBack()
                    }
                ) {
                    Text(text = "Deny")
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
                    .background(White)
            ) {
                if (isheaderExpanded) {
                    Text(text = "expanded")

                } else {
                    Text(text = "collapsed")
                }
            }

            RunMap(
                hasLocationPermission = viewModel.hasLocationPermission,
                cameraPositionState = cameraPositionState
            )
        }
    }
}

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
fun moveToUserLocation(
    fusedLocationClient: FusedLocationProviderClient,
    cameraPositionState: CameraPositionState
) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        cameraPositionState.move(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(location.latitude, location.longitude),
                20f
            )
        )
    }
    fusedLocationClient.lastLocation.addOnFailureListener {
        cameraPositionState.move(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(28.6139, 77.209),
                15f
            )
        )
    }
}
