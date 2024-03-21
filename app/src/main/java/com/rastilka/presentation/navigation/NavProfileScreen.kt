package com.rastilka.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.rastilka.presentation.navigation.navigation_models.AppNavGraph
import com.rastilka.presentation.navigation.navigation_models.NavigationScreens
import com.rastilka.presentation.screens.login_screen.LoginViewModel
import com.rastilka.presentation.screens.profile_screen.ProfileScreen

fun NavGraphBuilder.navProfileScreen(
    loginViewModel: LoginViewModel
) {
    navigation(
        route = AppNavGraph.ProfileGraph.rout,
        startDestination = NavigationScreens.ProfileScreen.rout,
    ) {
        composable(route = NavigationScreens.ProfileScreen.rout) {
            Column {
                ProfileScreen(loginViewModel)

            }
        }
    }
}