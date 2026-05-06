package com.`in`.routefinder.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.`in`.routefinder.presentation.components.MapContainer
import com.`in`.routefinder.presentation.components.SearchCard
import com.`in`.routefinder.presentation.components.SuggestionsSheet
import com.`in`.routefinder.presentation.model.LocationUi
import com.`in`.routefinder.presentation.viewModel.ActiveField
import com.`in`.routefinder.presentation.viewModel.MapUiState

@Composable
fun MapScreen(
    state: MapUiState,
    isPermissionGranted: Boolean,
    currentLocation: LatLng?,
    onStartQueryChange: (String) -> Unit,
    onDestinationQueryChange: (String) -> Unit,
    onStartSelected: (LocationUi) -> Unit,
    onDestinationSelected: (LocationUi) -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // ---------------- MAP ----------------

        MapContainer(
            modifier = Modifier.fillMaxSize(),
            isPermissionGranted = isPermissionGranted,
            currentLocation = currentLocation,
            startLocation = state.selectedStart,
            destinationLocation = state.selectedDestination,
            routePoints = state.routePoints
        )

        // ---------------- OVERLAY CONTENT ----------------

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
        ) {

            // SEARCH CARD

            SearchCard(
                state = state,
                onStartQueryChange = onStartQueryChange,
                onDestinationQueryChange = onDestinationQueryChange,
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 16.dp
                    )
            )

            // SUGGESTIONS

            val suggestions =
                when (state.activeField) {
                    ActiveField.START -> state.startSuggestions
                    ActiveField.DESTINATION -> state.destinationSuggestions
                }

            AnimatedVisibility(
                visible = suggestions.isNotEmpty()
            ) {

                SuggestionsSheet(
                    suggestions = suggestions,
                    onItemClick = { location ->

                        when (state.activeField) {

                            ActiveField.START -> {
                                onStartSelected(location)
                            }

                            ActiveField.DESTINATION -> {
                                onDestinationSelected(location)
                            }
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
        onDestinationSelected = {},
        isPermissionGranted = true,
        currentLocation = null
    )
}