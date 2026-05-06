package com.`in`.routefinder.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.`in`.routefinder.presentation.components.SearchCard
import com.`in`.routefinder.presentation.components.SuggestionsSheet
import com.`in`.routefinder.presentation.model.LocationUi
import com.`in`.routefinder.presentation.viewModel.ActiveField
import com.`in`.routefinder.presentation.viewModel.MapUiState

@Composable
fun MapScreen(
    state: MapUiState,
    onStartQueryChange: (String) -> Unit,
    onDestinationQueryChange: (String) -> Unit,
    onStartSelected: (LocationUi) -> Unit,
    onDestinationSelected: (LocationUi) -> Unit
) {
    val cameraPositionState = rememberCameraPositionState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // ---------------- MAP ----------------

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {

            // Start Marker
            state.selectedStart?.let { start ->

                val startMarkerState = rememberUpdatedMarkerState(
                    position = LatLng(start.lat, start.lng)
                )
                Marker(
                    state = startMarkerState,
                    title = start.name
                )
            }

            // Destination Marker
            state.selectedDestination?.let { dest ->

                val destinationMarkerState = rememberUpdatedMarkerState(
                    position = LatLng(dest.lat, dest.lng)
                )

                Marker(
                    state = destinationMarkerState,
                    title = dest.name
                )
            }

            // Route
            if (state.routePoints.isNotEmpty()) {

                Polyline(
                    points = state.routePoints,
                    width = 12f
                )
            }
        }

        // ---------------- TOP SEARCH ----------------

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
        ) {
            SearchCard(
                state = state,
                onStartQueryChange = onStartQueryChange,
                onDestinationQueryChange = onDestinationQueryChange,
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 24.dp
                    )
            )


            // ---------------- SUGGESTIONS ----------------

            val suggestions =
                if (state.activeField == ActiveField.START)
                    state.startSuggestions
                else
                    state.destinationSuggestions

            if (suggestions.isNotEmpty()) {

                SuggestionsSheet(
                    suggestions = suggestions,
                    onItemClick = {
                        if (state.activeField == ActiveField.START) {
                            onStartSelected(it)
                        } else {
                            onDestinationSelected(it)
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MapScreenPreview() {
    MapScreen(
        state = MapUiState(
            startQuery = "Koramangala",
            destinationQuery = "Airport",

            startSuggestions = listOf(
                LocationUi(
                    id = "1",
                    name = "Koramangala",
                    address = "Bangalore",
                    lat = 12.93,
                    lng = 77.62
                )
            )
        ),
        onStartQueryChange = {},
        onDestinationQueryChange = {},
        onStartSelected = {},
        onDestinationSelected = {}
    )
}