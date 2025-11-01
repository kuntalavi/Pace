package com.rk.pace.presentation.screens.run

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.rk.pace.presentation.screens.run.components.RunMap
import com.rk.pace.presentation.screens.run.components.RunTopB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunScreen(
    viewModel: RunViewModel = hiltViewModel(),
    goBack: () -> Unit
) {

    val segments = viewModel.pathPoints.collectAsState().value

    val context = LocalContext.current

    val cameraPositionState = rememberCameraPositionState()
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val requestLocationPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.setLocationPermission(true)
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
                requestLocationPermission.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RunTopB(
                goBack = goBack
            )
        }
    ) { it ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                RunMap(
                    hasLocationPermission = viewModel.hasLocationPermission,
                    cameraPositionState = cameraPositionState,
                    segments = segments
                )
            }

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
//    fusedLocationClient.lastLocation.addOnFailureListener {
//        cameraPositionState.move(
//            CameraUpdateFactory.newLatLngZoom(
//                LatLng(28.6139, 77.209),
//                15f
//            )
//        )
//    }
}
