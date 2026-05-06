package com.`in`.routefinder.presentation.viewModel

import com.google.android.gms.maps.model.LatLng
import com.`in`.routefinder.core.domain.util.AppError
import com.`in`.routefinder.presentation.model.LocationUi
import com.`in`.routefinder.presentation.model.RouteInfoUi

data class MapUiState(
    val startQuery: String = "",
    val destinationQuery: String = "",

    val activeField: ActiveField = ActiveField.START,

    val startSuggestions: List<LocationUi> = emptyList(),
    val destinationSuggestions: List<LocationUi> = emptyList(),

    val selectedStart: LocationUi? = null,
    val selectedDestination: LocationUi? = null,

    val currentLocation: LatLng? = null,

    val routePoints: List<LatLng> = emptyList(),

    val isLoading: Boolean = false,
    val error: AppError? = null,

    val routeInfo: RouteInfoUi? = null,
    val isRouteStarted: Boolean = false,

    val shouldStartTraversal: Boolean = false
)

enum class ActiveField {
    START, DESTINATION
}