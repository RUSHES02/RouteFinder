package com.`in`.routefinder.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.`in`.routefinder.presentation.components.MapContainer
import com.`in`.routefinder.presentation.components.RideProgressHeader
import com.`in`.routefinder.presentation.components.RouteInfoCard
import com.`in`.routefinder.presentation.components.SearchCard
import com.`in`.routefinder.presentation.components.SuggestionsSheet
import com.`in`.routefinder.presentation.model.LocationUi
import com.`in`.routefinder.presentation.viewModel.ActiveField
import com.`in`.routefinder.presentation.viewModel.MapUiState
import androidx.compose.animation.togetherWith

@Composable
fun MapScreen(
    state: MapUiState,
    isPermissionGranted: Boolean,
    currentLocation: LatLng?,
    onSearchFocusChanged: (Boolean) -> Unit,
    onActiveFieldChanged: (ActiveField) -> Unit,
    onStartQueryChange: (String) -> Unit,
    onDestinationQueryChange: (String) -> Unit,
    onStartSelected: (LocationUi) -> Unit,
    onDestinationSelected: (LocationUi) -> Unit,
    onStartRide: () -> Unit,
    onResetRoute: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
//    val context = LocalContext.current
//    LaunchedEffect(state.error) {
//        state.error?.let {
//            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
//        }
//    }
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
            routePoints = state.routePoints,
            shouldStartTraversal = state.shouldStartTraversal
        )

        // ---------------- OVERLAY CONTENT ----------------
        AnimatedContent(
            targetState =  state.selectedStart != null &&
                    state.selectedDestination != null,
            transitionSpec = {
                slideInVertically(
                    initialOffsetY = { fullHeight ->
                        -fullHeight
                    }
                ) + fadeIn() togetherWith(
                    slideOutVertically(
                        targetOffsetY = { fullHeight ->
                            -fullHeight
                        }
                    ) + fadeOut()
                )
            },
            label = "ride_ui_transition"
        ) { isRideStarted ->
            if (!isRideStarted) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .statusBarsPadding()
                ) {
                    // ---------------- SEARCH CARD ----------------
                    SearchCard(
                        state = state,
                        onActiveFieldChanged = onActiveFieldChanged,
                        onStartQueryChange = onStartQueryChange,
                        onDestinationQueryChange = onDestinationQueryChange,
                        onSearchFocusChanged = onSearchFocusChanged,
                        modifier = Modifier
                            .padding(
                                horizontal = 16.dp,
                                vertical = 16.dp
                            )
                    )

                    // ---------------- SUGGESTIONS ----------------
                    val suggestions =
                        when (state.activeField) {
                            ActiveField.START ->
                                state.startSuggestions

                            ActiveField.DESTINATION ->
                                state.destinationSuggestions
                        }

                    val suggestionsWithCurrentLocation = buildList {
                        currentLocation?.let {
                            add(
                                LocationUi(
                                    id = "current_location",
                                    name = "Current Location",
                                    address = "Use your current location",
                                    lat = it.latitude,
                                    lng = it.longitude,
                                    isCurrentLocation = true
                                )
                            )
                        }
                        addAll(suggestions)
                    }
                    AnimatedVisibility(
                        visible = state.isSearchFocused &&
                                suggestionsWithCurrentLocation.isNotEmpty(),
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        SuggestionsSheet(
                            suggestions = suggestionsWithCurrentLocation,
                            onItemClick = { location ->
                                keyboardController?.hide()
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
            } else {
                RideProgressHeader(
                    start = state.selectedStart?.name.orEmpty(),
                    destination = state.selectedDestination?.name.orEmpty(),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .statusBarsPadding()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 16.dp
                        )
                )
            }
        }

        state.routeInfo?.let {
//            if (state.shouldStartTraversal) {
                RouteInfoCard(
                    routeInfo = it,
                    isRideStarted = state.shouldStartTraversal,
                    onStartClick = onStartRide,
                    onResetClick = onResetRoute,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
//            }
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
        currentLocation = null,
        onStartRide = {},
        onResetRoute = {},
        onSearchFocusChanged = {},
        onActiveFieldChanged = {}
    )
}