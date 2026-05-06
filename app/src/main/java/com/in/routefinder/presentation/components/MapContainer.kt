package com.`in`.routefinder.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.`in`.routefinder.presentation.model.LocationUi

@Composable
fun MapContainer(
    modifier: Modifier = Modifier,
    isPermissionGranted: Boolean,
    currentLocation: LatLng?,
    startLocation: LocationUi?,
    destinationLocation: LocationUi?,
    routePoints: List<LatLng>
) {

    val context = LocalContext.current

    // ---------------- MAP UI SETTINGS ----------------

    val mapUiSettings by remember {
        mutableStateOf(
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
        )
    }

    // ---------------- MAP PROPERTIES ----------------

    val mapProperties = remember(isPermissionGranted) {
        MapProperties(
            isMyLocationEnabled = isPermissionGranted,
            mapType = MapType.NORMAL,
            latLngBoundsForCameraTarget = LatLngBounds(
                LatLng(8.07, 68.12),
                LatLng(37.1, 97.42)
            ),
            maxZoomPreference = 21f,
            minZoomPreference = 5f
        )
    }

    // ---------------- INITIAL CAMERA ----------------

    val initialPosition = remember(currentLocation) {

        currentLocation ?: LatLng(
            12.9716,
            77.5946
        ) // fallback Bangalore
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            initialPosition,
            14f
        )
    }

    // ---------------- MAP ----------------

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        properties = mapProperties,
        uiSettings = mapUiSettings,
        cameraPositionState = cameraPositionState
    ) {

        // START MARKER

        startLocation?.let { start ->

            Marker(
                state = rememberUpdatedMarkerState(
                    position = LatLng(start.lat, start.lng)
                ),
                title = start.name
            )
        }

        // DESTINATION MARKER

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

        // ROUTE

        if (routePoints.isNotEmpty()) {

            Polyline(
                points = routePoints,
                width = 12f
            )
        }
    }
}