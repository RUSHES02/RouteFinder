package com.`in`.routefinder.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TripOrigin
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.`in`.routefinder.R
import com.`in`.routefinder.presentation.model.LocationUi
import com.`in`.routefinder.ui.theme.colorAccent
import com.`in`.routefinder.ui.theme.colorHighlight
import com.`in`.routefinder.ui.theme.colorPrimary
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.atan2

@Composable
fun MapContainer(
    modifier: Modifier = Modifier,
    isPermissionGranted: Boolean,
    currentLocation: LatLng?,
    startLocation: LocationUi?,
    destinationLocation: LocationUi?,
    routePoints: List<LatLng>,
    shouldStartTraversal: Boolean
) {
    val context = LocalContext.current
    // ---------------- MAP SETTINGS ----------------
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

    val mapProperties = remember(isPermissionGranted) {
        MapProperties(
            isMyLocationEnabled = isPermissionGranted,
            mapType = MapType.NORMAL,
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                context,
                R.raw.map_style_json
            ),
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
        LatLng(12.9716, 77.5946)
    }

    // ---------------- INITIAL VEHICLE POSITION ----------------

    val initialVehiclePosition = remember(routePoints) {
        routePoints.firstOrNull()
            ?: currentLocation
            ?: defaultLocation
    }

    // ---------------- CAMERA ----------------
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            currentLocation ?: defaultLocation,
            14f
        )
    }

    // ---------------- ANIMATION ----------------
    val animatedLat = remember {
        Animatable(initialVehiclePosition.latitude.toFloat())
    }

    val animatedLng = remember {
        Animatable(initialVehiclePosition.longitude.toFloat())
    }

    var vehicleRotation by remember {
        mutableFloatStateOf(0f)
    }

    val animatedVehiclePosition by remember {
        derivedStateOf {
            LatLng(
                animatedLat.value.toDouble(),
                animatedLng.value.toDouble()
            )
        }
    }

    // ---------------- CAMERA TO CURRENT LOCATION ----------------
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

    // ---------------- CAMERA TO START ---------------
    LaunchedEffect(startLocation) {
        startLocation?.let {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(it.lat, it.lng),
                    15f
                ),
                durationMs = 800
            )
        }
    }

    // ---------------- FIT ROUTE ----------------
    LaunchedEffect(routePoints) {
        if (routePoints.isEmpty()) return@LaunchedEffect
        val boundsBuilder = LatLngBounds.builder()
        routePoints.forEach {
            boundsBuilder.include(it)
        }
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngBounds(
                boundsBuilder.build(),
                120
            ),
            durationMs = 1000
        )
    }

    // ---------------- ROUTE TRAVERSAL ----------------
    val routeStartPoint = remember(routePoints) {
        routePoints.firstOrNull()
    }

    val routeEndPoint = remember(routePoints) {
        routePoints.lastOrNull()
    }
    var isTraversalCompleted by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(
        routePoints,
        shouldStartTraversal
    ) {
        if (!shouldStartTraversal) return@LaunchedEffect

        if (routePoints.size < 2) return@LaunchedEffect
        isTraversalCompleted = false
        for (i in 0 until routePoints.lastIndex) {

            val start = routePoints[i]
            val end = routePoints[i + 1]

            vehicleRotation = calculateBearing(
                start,
                end
            )

            coroutineScope {
                launch {
                    animatedLat.animateTo(
                        targetValue = end.latitude.toFloat(),
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing
                        )
                    )
                }

                launch {
                    animatedLng.animateTo(
                        targetValue = end.longitude.toFloat(),
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing
                        )
                    )
                }
            }
        }
        isTraversalCompleted = true
    }

    // ---------------- CAMERA FOLLOW ----------------
    LaunchedEffect(animatedVehiclePosition) {
        if (!shouldStartTraversal) return@LaunchedEffect

        cameraPositionState.position =
            CameraPosition.fromLatLngZoom(
                animatedVehiclePosition,
                16f
            )
    }

    // ---------------- MAP ----------------

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        properties = mapProperties,
        uiSettings = mapUiSettings,
        cameraPositionState = cameraPositionState,
        contentPadding = PaddingValues(
            top = 150.dp,
            bottom = 150.dp
        )
    ) {

        // ---------------- START LOCATION ----------------
        startLocation?.let { start ->
            MarkerComposable(
                state = rememberUpdatedMarkerState(
                    position = routeStartPoint
                        ?: LatLng(
                            start.lat,
                            start.lng
                        )
                ),
                title = start.name
            ) {
                Icon(
                    modifier = Modifier
                        .size(25.dp),
                    imageVector = Icons.Default.TripOrigin,
                    contentDescription = null,
                    tint = colorAccent
                )
            }
        }

        // ---------------- DESTINATION ----------------
        destinationLocation?.let { destination ->
            Marker(
                state = rememberUpdatedMarkerState(
                    position = routeEndPoint
                        ?: LatLng(
                            destination.lat,
                            destination.lng
                        )
                ),
                title = destination.name,
                draggable = false
            ) {  }
        }

        // ---------------- ROUTE ----------------
        if (routePoints.isNotEmpty()) {
            Polyline(
                points = routePoints,
                width = 12f,
                color = colorPrimary
            )
        }

        // ---------------- VEHICLE ----------------
        if (routePoints.isNotEmpty() && shouldStartTraversal && !isTraversalCompleted) {
            MarkerComposable(
                state = rememberUpdatedMarkerState(
                    position = animatedVehiclePosition
                ),
                title = "Vehicle",
                rotation = vehicleRotation,
                flat = true,
            ) {
                Icon(
                    modifier = Modifier
                        .size(50.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_navigation),
                    contentDescription = null,
                    tint = colorHighlight
                )
            }
        }
    }
}

private fun calculateBearing(
    start: LatLng,
    end: LatLng
): Float {

    val latDiff = end.latitude - start.latitude
    val lngDiff = end.longitude - start.longitude

    return Math.toDegrees(
        atan2(
            lngDiff,
            latDiff
        )
    ).toFloat()
}
