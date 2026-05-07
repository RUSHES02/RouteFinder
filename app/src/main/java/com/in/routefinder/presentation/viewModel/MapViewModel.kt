package com.`in`.routefinder.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.`in`.routefinder.core.domain.util.Response
import com.`in`.routefinder.domain.MapsRepository
import com.`in`.routefinder.presentation.mapper.toDomain
import com.`in`.routefinder.presentation.mapper.toUi
import com.`in`.routefinder.presentation.model.LocationUi
import com.`in`.routefinder.presentation.utils.CurrentLocationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: MapsRepository,
    private val currentLocationProvider: CurrentLocationProvider
) : ViewModel() {

    private val _state = MutableStateFlow(MapUiState())
    val state: StateFlow<MapUiState> = _state

    private val searchQuery = MutableStateFlow("")

    init {
        observeSearch()
    }

    fun onLocationPermissionResult(
        isGranted: Boolean
    ) {
        if (!isGranted) return
        viewModelScope.launch {
            val location = currentLocationProvider.getCurrentLocation()
            _state.update {
                it.copy(
                    currentLocation = location
                )
            }
        }
    }

    // -------------------------
    // SEARCH (DEBOUNCE)
    // -------------------------
    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch {
            searchQuery
                .debounce(400)
                .distinctUntilChanged()
                .collectLatest { query ->

                    val activeField = _state.value.activeField

                    if (query.isBlank()) {
                        clearSuggestions(activeField)
                        return@collectLatest
                    }

                    _state.update { it.copy(isLoading = true) }

                    when (val result = repository.searchLocations(query)) {

                        is Response.Success -> {
                            val mapped = result.data.map { it.toUi() }

                            _state.update {
                                when (activeField) {
                                    ActiveField.START -> it.copy(
                                        startSuggestions = mapped,
                                        isLoading = false
                                    )

                                    ActiveField.DESTINATION -> it.copy(
                                        destinationSuggestions = mapped,
                                        isLoading = false
                                    )
                                }
                            }
                        }

                        is Response.Error -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = result.error
                                )
                            }
                        }
                    }
                }
        }
    }

    // -------------------------
    // INPUT HANDLERS
    // -------------------------
    fun onSearchFocusChanged(
        isFocused: Boolean,
    ) {
        _state.update {
            it.copy(
                isSearchFocused = isFocused
            )
        }
    }

    fun onActiveFieldChanged(
        field: ActiveField
    ) {
        _state.update {
            it.copy(
                activeField = field
            )
        }
    }

    fun onStartQueryChange(query: String) {
        _state.update {
            it.copy(
                startQuery = query,
                activeField = ActiveField.START
            )
        }
        searchQuery.value = query
    }

    fun onDestinationQueryChange(query: String) {
        _state.update {
            it.copy(
                destinationQuery = query,
                activeField = ActiveField.DESTINATION
            )
        }
        searchQuery.value = query
    }

    // -------------------------
    // SELECTION
    // -------------------------

    fun onStartSelected(
        location: LocationUi
    ) {
        // ---------------- CURRENT LOCATION ---------------
        if (location.isCurrentLocation) {
            _state.update {
                it.copy(
                    activeField = ActiveField.START,
                    selectedStart = location,
                    startSuggestions = emptyList(),
                    startQuery = location.name,
                    isSearchFocused = false
                )
            }
            checkAndFetchRoute()
            return
        }

        // ---------------- NORMAL LOCATION ----------------
        _state.update {
            it.copy(
                selectedStart = location,
                startQuery = location.name,
                startSuggestions = emptyList(),
                isSearchFocused = false
            )
        }
        fetchDetails(location = location, isStart = true)
    }

    fun onDestinationSelected(
        location: LocationUi
    ) {
        // ---------------- CURRENT LOCATION ----------------
        if (location.isCurrentLocation) {
            _state.update {
                it.copy(
                    activeField = ActiveField.DESTINATION,
                    selectedDestination = location,
                    destinationSuggestions = emptyList(),
                    destinationQuery = location.name,
                    isSearchFocused = false
                )
            }
            checkAndFetchRoute()
            return
        }

        // ---------------- NORMAL LOCATION ----------------
        _state.update {
            it.copy(
                selectedDestination = location,
                destinationQuery = location.name,
                destinationSuggestions = emptyList(),
                isSearchFocused = false
            )
        }
        fetchDetails(location = location, isStart = false)
    }

    private fun fetchDetails(location: LocationUi, isStart: Boolean) {
        viewModelScope.launch {

            _state.update { it.copy(isLoading = true) }

            when (val result = repository.getLocationDetails(
                placeId = location.id,
                name = location.name
            )) {

                is Response.Success -> {
                    val loc = result.data.toUi()

                    _state.update {
                        if (isStart) {
                            it.copy(
                                selectedStart = loc,
                                startSuggestions = emptyList(),
                                startQuery = loc.name,
                                isLoading = false
                            )
                        } else {
                            it.copy(
                                selectedDestination = loc,
                                destinationSuggestions = emptyList(),
                                destinationQuery = loc.name,
                                isLoading = false
                            )
                        }
                    }

                    checkAndFetchRoute()
                }

                is Response.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                }
            }
        }
    }

    // -------------------------
    // ROUTE FETCH
    // -------------------------

    private fun checkAndFetchRoute() {
        val start = _state.value.selectedStart
        val destination = _state.value.selectedDestination

        if (start != null && destination != null) {
            fetchRoute(start, destination)
        }
    }

    private fun fetchRoute(start: LocationUi, dest: LocationUi) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = repository.getRoute(
                start = start.toDomain(),
                destination = dest.toDomain()
            )) {
                is Response.Success -> {
                    val points = result.data.points.map {
                        LatLng(it.latitude, it.longitude)
                    }
                    _state.update {
                        it.copy(
                            routePoints = points,
                            routeInfo = result.data.routeInfo?.toUi(),
                            isLoading = false
                        )
                    }
                }

                is Response.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.error
                        )
                    }
                }
            }
        }
    }

    fun onStartRide() {
        _state.update {
            it.copy(
                shouldStartTraversal = true
            )
        }
    }

    fun onResetRoute() {
        _state.update {
            it.copy(
                startQuery = "",
                destinationQuery = "",
                startSuggestions = emptyList(),
                activeField = ActiveField.START,
                destinationSuggestions = emptyList(),
                selectedStart = null,
                selectedDestination = null,
                routePoints = emptyList(),
                routeInfo = null,
                shouldStartTraversal = false,
                error = null
            )
        }
    }

    // -------------------------
    // HELPERS
    // -------------------------

    private fun clearSuggestions(field: ActiveField) {
        _state.update {
            when (field) {
                ActiveField.START -> it.copy(startSuggestions = emptyList())
                ActiveField.DESTINATION -> it.copy(destinationSuggestions = emptyList())
            }
        }
    }
}