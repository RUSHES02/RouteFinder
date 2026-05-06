package com.`in`.routefinder.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.`in`.routefinder.presentation.model.RouteInfoUi
import com.`in`.routefinder.ui.theme.colorPrimary
import com.`in`.routefinder.ui.theme.colorWhite

@Composable
fun RouteInfoCard(
    routeInfo: RouteInfoUi,
    isRideStarted: Boolean,
    onStartClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(40f),
        colors = CardDefaults.elevatedCardColors(
            containerColor = colorPrimary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = routeInfo.distanceText,
                style = MaterialTheme.typography.titleMedium,
                color = colorWhite,
            )

            Text(
                text = routeInfo.durationText,
                style = MaterialTheme.typography.bodyMedium,
                color = colorWhite
            )

            if (!isRideStarted) {
                CustomButton(
                    text = "Start Ride",
                    onClick = onStartClick,
                    buttonType = ButtonType.PRIMARY,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            } else {
                Text(
                    text = "Ride in Progress...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorPrimary
                )

                CustomButton(
                    text = "Reset",
                    onClick = onResetClick,
                    buttonType = ButtonType.PRIMARY,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
private fun RouteInfoCardPreview() {
    RouteInfoCard(
        routeInfo = RouteInfoUi(
            distanceText = "distance",
            durationText = "duration"
        ),
        isRideStarted = false,
        onStartClick = {  },
        onResetClick = {  }
    )
}