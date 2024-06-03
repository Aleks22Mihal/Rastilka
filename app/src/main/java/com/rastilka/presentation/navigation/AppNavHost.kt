package com.rastilka.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.rastilka.presentation.navigation.navigation_models.AppNavGraph
import com.rastilka.presentation.screens.login_screen.LoginViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    innerPadding: PaddingValues,
) {
    NavHost(
        navController = navController,
        startDestination = AppNavGraph.FamilyGraph.rout,
        modifier = Modifier
            .consumeWindowInsets(innerPadding)
            .padding(innerPadding)
    ) {
        navFamilyScreen()

        navFamilyTasksScreen(navController = navController)

        navWishesScreen(navController = navController)

        navScoresScreen()

        navProfileScreen(
            loginViewModel = loginViewModel,
            navController = navController
        )

    }
}
