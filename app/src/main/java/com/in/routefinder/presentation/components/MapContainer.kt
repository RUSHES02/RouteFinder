package com.`in`.routefinder.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.`in`.routefinder.presentation.model.LocationUi
import kotlinx.coroutines.delay

@Composable
fun MapContainer(
    modifier: Modifier = Modifier,
    isPermissionGranted: Boolean,
    currentLocation: LatLng?,
    startLocation: LocationUi?,
    destinationLocation: LocationUi?,
    routePoints: List<LatLng>
) {
    // ---------------- MAP UI SETTINGS ----------------
    val mapUiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false,
            rotationGesturesEnabled = false,
            zoomGesturesEnabled = true,
            scrollGesturesEnabled = true,
            scrollGesturesEnabledDuringRotateOrZoom = false,
            mapToolbarEnabled = false,
            myLocationButtonEnabled = false,
            tiltGesturesEnabled = false
        )
    }

    // ---------------- MAP PROPERTIES ----------------
    val mapProperties = remember(isPermissionGranted) {
        MapProperties(
            isMyLocationEnabled = isPermissionGranted,
            mapType = MapType.NORMAL,
//            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
//                context,
//                R.raw.map_style_json
//            ),
            latLngBoundsForCameraTarget = LatLngBounds(
                LatLng(8.07, 68.12),
                LatLng(37.1, 97.42)
            ),
            maxZoomPreference = 21f,
            minZoomPreference = 5f
        )
    }

    // ---------------- DEFAULT LOCATION ----------------
    val defaultLocation = remember {
        LatLng(12.9716, 77.5946) // Bangalore fallback
    }

    // ---------------- CAMERA ----------------
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            currentLocation ?: defaultLocation,
            14f
        )
    }

    // ---------------- VEHICLE MARKER ----------------
    val vehicleMarkerState = remember {
        MarkerState(
            position = currentLocation ?: defaultLocation
        )
    }

    // ---------------- MOVE CAMERA TO CURRENT LOCATION ----------------

    LaunchedEffect(currentLocation) {
        currentLocation?.let {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    it,
                    14f
                ),
                durationMs = 1000
            )
        }
    }

    // ---------------- ROUTE ANIMATION ----------------
    LaunchedEffect(routePoints) {
        if (routePoints.size < 2) return@LaunchedEffect
        for (i in 0 until routePoints.lastIndex) {
            val start = routePoints[i]
            val end = routePoints[i + 1]
            val steps = 30
            for (step in 0..steps) {
                val fraction = step / steps.toFloat()
                val interpolatedPosition = interpolate(
                    fraction = fraction,
                    start = start,
                    end = end
                )
                // Move vehicle marker
                vehicleMarkerState.position = interpolatedPosition
                // Smooth camera follow
                cameraPositionState.position =
                    CameraPosition.fromLatLngZoom(
                        interpolatedPosition,
                        15f
                    )

                delay(5L)
            }
        }
    }

    // ---------------- MAP ----------------
    GoogleMap(
        modifier = modifier.fillMaxSize(),
        properties = mapProperties,
        uiSettings = mapUiSettings,
        cameraPositionState = cameraPositionState
    ) {

        // ---------------- START MARKER ----------------
        startLocation?.let { start ->
            Marker(
                state = rememberUpdatedMarkerState(
                    position = LatLng(
                        start.lat,
                        start.lng
                    )
                ),
                title = start.name
            )
        }

        // ---------------- DESTINATION MARKER ----------------
        destinationLocation?.let { destination ->
            Marker(
                state = rememberUpdatedMarkerState(
                    position = LatLng(
                        destination.lat,
                        destination.lng
                    )
                ),
                title = destination.name
            )
        }

        // ---------------- ROUTE POLYLINE ----------------
        if (routePoints.isNotEmpty()) {
            Polyline(
                points = routePoints,
                width = 12f
            )
        }

        // ---------------- ANIMATED VEHICLE MARKER ----------------
        if (routePoints.isNotEmpty()) {
            Marker(
                state = vehicleMarkerState,
                title = "Vehicle"
            )
        }
    }
}

/**
 * Smooth interpolation between two LatLng points
 */
private fun interpolate(
    fraction: Float,
    start: LatLng,
    end: LatLng
): LatLng {

    return LatLng(
        start.latitude + (end.latitude - start.latitude) * fraction,
        start.longitude + (end.longitude - start.longitude) * fraction
    )
}
