package com.rk.pace.data.tracking

import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority

object LocationRequest {
    private const val LOCATION_UPDATE_INTERVAL = 3000L

    val locationRequest
        get() = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            LOCATION_UPDATE_INTERVAL
        )

//    fun checkAndRequestLocationSettings(
//        activity: Activity,
//        launcher: ActivityResultLauncher<IntentSenderRequest>
//    ) {
//        val locationRequest = locationRequest.build()
//
//        val locationSettingsRequest =
//            LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest)
//                .build()
//
//        val client: SettingsClient = LocationServices.getSettingsClient(activity)
//
//        client.checkLocationSettings(locationSettingsRequest)
//            .addOnFailureListener { exception ->
//                if (exception is ResolvableApiException) {
//                    try {
//                        val intentSenderRequest =
//                            IntentSenderRequest.Builder(exception.resolution).build()
//
//                        launcher.launch(intentSenderRequest)
//
//                    } catch (_: IntentSender.SendIntentException) {
//                    }
//                }
//            }
//    }
}