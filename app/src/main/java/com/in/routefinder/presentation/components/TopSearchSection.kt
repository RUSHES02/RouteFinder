package com.`in`.routefinder.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.`in`.routefinder.presentation.model.LocationUi
import com.`in`.routefinder.presentation.viewModel.MapUiState

@Composable
fun TopSearchSection(
    state: MapUiState,
    onStartQueryChange: (String) -> Unit,
    onDestinationQueryChange: (String) -> Unit,
    onStartSelected: (LocationUi) -> Unit,
    onDestinationSelected: (LocationUi) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .   padding(12.dp)
    ) {

        SearchTextField(
            value = state.startQuery,
            onValueChange = onStartQueryChange,
            placeholder = "Start location"
        )

        Spacer(
            modifier = Modifier
                .height(8.dp)
        )

        SearchTextField(
            value = state.destinationQuery,
            onValueChange = onDestinationQueryChange,
            placeholder = "Destination"
        )

        Spacer(modifier = Modifier.height(8.dp))

        when {
            state.startSuggestions.isNotEmpty() -> {
                SuggestionsList(
                    suggestions = state.startSuggestions,
                    onItemClick = onStartSelected
                )
            }

            state.destinationSuggestions.isNotEmpty() -> {
                SuggestionsList(
                    suggestions = state.destinationSuggestions,
                    onItemClick = onDestinationSelected
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopSearchSectionPreview() {
    TopSearchSection(
        state = MapUiState(
            startSuggestions = listOf(
                LocationUi(
                    id = "1",
                    name = "Bangalore",
                    address = "Karnataka",
                    lat = 0.0,
                    lng = 0.0
                )
            )
        ),
        onStartQueryChange = {},
        onDestinationQueryChange = {},
        onStartSelected = {},
        onDestinationSelected = {}
    )
}