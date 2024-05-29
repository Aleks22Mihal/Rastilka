package com.rastilka.presentation.navigation

import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.rastilka.presentation.navigation.navigation_models.AppNavGraph
import com.rastilka.presentation.navigation.navigation_models.NavigationScreens
import com.rastilka.presentation.screens.family_screen.FamilyScreen
import com.rastilka.presentation.screens.family_screen.FamilyViewModel

fun NavGraphBuilder.navFamilyScreen() {
    navigation(
        route = AppNavGraph.FamilyGraph.rout,
        startDestination = NavigationScreens.FamilyScreen.rout,
    ) {
        composable(route = NavigationScreens.FamilyScreen.rout) {

            val viewModel = hiltViewModel<FamilyViewModel>()

            FamilyScreen(
                state = viewModel.state.collectAsState(),
                onEvent = viewModel::onEvent
            )
        }
    }
}