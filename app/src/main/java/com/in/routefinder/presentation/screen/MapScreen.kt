package com.`in`.routefinder.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.maps.android.compose.GoogleMap
import com.`in`.routefinder.presentation.components.TopSearchSection
import com.`in`.routefinder.presentation.viewModel.MapUiState

@Composable
fun MapScreen(
    state: MapUiState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Map (background)
        GoogleMap {

        }
        // Search UI (top overlay)
        TopSearchSection(
            state = state,
            onStartQueryChange = {},
            onDestinationQueryChange = {},
            onStartSelected = {},
            onDestinationSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    MapScreen(
        state = MapUiState()
    )
}