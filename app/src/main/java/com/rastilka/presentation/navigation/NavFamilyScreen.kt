package com.rastilka.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.rastilka.presentation.navigation.navigation_models.AppNavGraph
import com.rastilka.presentation.navigation.navigation_models.NavigationScreens
import com.rastilka.presentation.screens.family_screen.FamilyScreen

fun NavGraphBuilder.navFamilyScreen() {
    navigation(
        route = AppNavGraph.FamilyGraph.rout,
        startDestination = NavigationScreens.FamilyScreen.rout,
    ) {
        composable(route = NavigationScreens.FamilyScreen.rout) {
            FamilyScreen()
        }
    }
}