package com.`in`.routefinder.presentation.navigation

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.`in`.routefinder.presentation.screen.MapScreen
import com.`in`.routefinder.presentation.viewModel.MapViewModel
import kotlinx.serialization.Serializable

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(), ) {

    NavHost(
        navController = navController,
        startDestination = MapRoute
    ) {

        composable<MapRoute> {
            val viewModel = hiltViewModel<MapViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()



            val locationPermissionState =
                rememberPermissionState(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )

            // ---------------- REQUEST PERMISSION ----------------

            LaunchedEffect(Unit) {
                locationPermissionState.launchPermissionRequest()
            }

            // ---------------- NOTIFY VIEWMODEL ----------------

            LaunchedEffect(locationPermissionState.status.isGranted) {
                viewModel.onLocationPermissionResult(
                    isGranted = locationPermissionState.status.isGranted
                )
            }

            // ---------------- REQUEST PERMISSION ----------------

            LaunchedEffect(Unit) {
                locationPermissionState.launchPermissionRequest()
            }

            MapScreen(
                state = state,
                onSearchFocusChanged = viewModel::onSearchFocusChanged,
                onActiveFieldChanged = viewModel::onActiveFieldChanged,
                onStartQueryChange = viewModel::onStartQueryChange,
                onDestinationQueryChange = viewModel::onDestinationQueryChange,
                onStartSelected = viewModel::onStartSelected,
                onDestinationSelected = viewModel::onDestinationSelected,
                isPermissionGranted = locationPermissionState.status.isGranted,
                onStartRide = viewModel::onStartRide,
                currentLocation = state.currentLocation,
                onResetRoute = viewModel::onResetRoute
            )
        }
    }
}

@Serializable
data object MapRoute