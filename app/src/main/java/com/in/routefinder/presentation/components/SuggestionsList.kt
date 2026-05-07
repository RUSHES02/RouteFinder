package com.`in`.routefinder.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.`in`.routefinder.presentation.model.LocationUi
import com.`in`.routefinder.ui.theme.colorAccent1
import com.`in`.routefinder.ui.theme.colorPrimary
import com.`in`.routefinder.ui.theme.colorWhite

@Composable
fun SuggestionsSheet(
    suggestions: List<LocationUi>,
    onItemClick: (LocationUi) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(40f),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = colorPrimary
        )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp),
        ) {
            items(suggestions) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onItemClick(item)
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector =
                            if (item.isCurrentLocation)
                                Icons.Default.MyLocation
                            else
                                Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = colorWhite
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (item.isCurrentLocation)
                                    FontWeight.SemiBold
                                else
                                    FontWeight.Normal,
                            color = colorWhite,
                        )

                        Text(
                            text = item.address,
                            style = MaterialTheme.typography.bodySmall,
                            color = colorAccent1,
                            maxLines = 1
                        )
                    }
                }

                HorizontalDivider(color = colorWhite)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SuggestionsListPreview() {
    SuggestionsSheet(
        suggestions = listOf(
            LocationUi("1", "Bangalore", "Karnataka", 0.0, 0.0),
            LocationUi("2", "Mumbai", "Maharashtra", 0.0, 0.0)
        ),
        onItemClick = {}
    )
}