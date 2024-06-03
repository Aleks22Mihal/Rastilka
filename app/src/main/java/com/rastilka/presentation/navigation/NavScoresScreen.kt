package com.rastilka.presentation.navigation

import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.rastilka.presentation.navigation.navigation_models.AppNavGraph
import com.rastilka.presentation.navigation.navigation_models.NavigationScreens
import com.rastilka.presentation.screens.transaction_screen.TransactionScreen
import com.rastilka.presentation.screens.transaction_screen.TransactionViewModel

fun NavGraphBuilder.navScoresScreen() {
    navigation(
        route = AppNavGraph.ScoresGraph.rout,
        startDestination = NavigationScreens.ScoresScreen.rout,
    ) {
        composable(route = NavigationScreens.ScoresScreen.rout) {
            val viewModel = hiltViewModel<TransactionViewModel>()

            TransactionScreen(
                state = viewModel.state.collectAsState(),
                onEvent = viewModel::onEvent
            )
        }
    }
}
