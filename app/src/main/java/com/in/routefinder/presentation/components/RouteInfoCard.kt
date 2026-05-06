package com.`in`.routefinder.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.`in`.routefinder.presentation.model.RouteInfoUi

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
        shape = RoundedCornerShape(24.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = routeInfo.distanceText,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = routeInfo.durationText,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!isRideStarted) {

                Button(
                    onClick = onStartClick,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Start Ride")
                }

            } else {

                Text(
                    text = "Ride in Progress...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF2E7D32)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onResetClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reset")
                }
            }
        }
    }
}