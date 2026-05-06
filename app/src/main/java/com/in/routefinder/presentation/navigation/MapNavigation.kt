package com.`in`.routefinder.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.`in`.routefinder.presentation.screen.MapScreen
import com.`in`.routefinder.presentation.viewModel.MapViewModel
import kotlinx.serialization.Serializable

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = MapRoute
    ) {

        composable<MapRoute> {
            val viewModel = hiltViewModel<MapViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            MapScreen(
                state = state,
                onStartQueryChange = viewModel::onStartQueryChange,
                onDestinationQueryChange = viewModel::onDestinationQueryChange,
                onStartSelected = viewModel::onStartSelected,
                onDestinationSelected = viewModel::onDestinationSelected
            )
        }
    }
}

@Serializable
data object MapRoute