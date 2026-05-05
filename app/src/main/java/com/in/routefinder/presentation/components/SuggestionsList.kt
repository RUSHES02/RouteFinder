package com.`in`.routefinder.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.`in`.routefinder.presentation.model.LocationUi

@Composable
fun SuggestionsList(
    suggestions: List<LocationUi>,
    onItemClick: (LocationUi) -> Unit
) {
    LazyColumn {
        items(suggestions) { item ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item) }
                    .padding(12.dp)
            ) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.address,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SuggestionsListPreview() {
    SuggestionsList(
        suggestions = listOf(
            LocationUi("1", "Bangalore", "Karnataka", 0.0, 0.0),
            LocationUi("2", "Mumbai", "Maharashtra", 0.0, 0.0)
        ),
        onItemClick = {}
    )
}