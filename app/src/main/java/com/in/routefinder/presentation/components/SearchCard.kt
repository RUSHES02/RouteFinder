package com.`in`.routefinder.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.`in`.routefinder.presentation.viewModel.ActiveField
import com.`in`.routefinder.presentation.viewModel.MapUiState
import com.`in`.routefinder.ui.theme.colorPrimary
import com.`in`.routefinder.ui.theme.colorWhite

@Composable
fun SearchCard(
    state: MapUiState,
    onActiveFieldChanged: (ActiveField) -> Unit,
    onStartQueryChange: (String) -> Unit,
    onDestinationQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearchFocusChanged: (Boolean) -> Unit
) {

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(40f),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = colorPrimary
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SearchField(
                value = state.startQuery,
                placeholder = "Starting point",
                leadingIcon = Icons.Default.MyLocation,
                onValueChange = onStartQueryChange,
                modifier = Modifier.onFocusChanged {
                    onSearchFocusChanged(
                        it.isFocused
                    )
                    if (it.isFocused) {
                        onActiveFieldChanged(
                            ActiveField.START
                        )
                    }
                }
            )

            HorizontalDivider(color = colorWhite)

            SearchField(
                value = state.destinationQuery,
                placeholder = "Where to?",
                leadingIcon = Icons.Default.LocationOn,
                onValueChange = onDestinationQueryChange,
                modifier = Modifier.onFocusChanged {
                    onSearchFocusChanged(
                        it.isFocused
                    )
                    if (it.isFocused) {
                        onActiveFieldChanged(
                            ActiveField.DESTINATION
                        )
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchCardPreview() {
    SearchCard(
        state = MapUiState(),
        onStartQueryChange = {},
        onDestinationQueryChange = {},
        onSearchFocusChanged = {},
        onActiveFieldChanged = {}
    )
}