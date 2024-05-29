package com.rastilka.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.rastilka.presentation.navigation.navigation_models.AppNavGraph
import com.rastilka.presentation.navigation.navigation_models.NavigationScreens
import com.rastilka.presentation.screens.create_wish_screen.CreateWishScreen
import com.rastilka.presentation.screens.family_wishes_screen.FamilyWishesScreen
import com.rastilka.presentation.screens.family_wishes_screen.FamilyWishesViewModel

fun NavGraphBuilder.navWishesScreen(navController: NavHostController) {
    navigation(
        route = AppNavGraph.WishesGraph.rout,
        startDestination = NavigationScreens.WishScreen.rout,
    ) {
        composable(route = NavigationScreens.WishScreen.rout) {

            val viewModel = hiltViewModel<FamilyWishesViewModel>()

            FamilyWishesScreen(
                state = viewModel.state.collectAsState(),
                onEvent = viewModel::onEvent,
                navController = navController
            )
        }
        composable(
            route = NavigationScreens.CreateWishScreen.rout,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            CreateWishScreen(navController = navController)
        }
    }
}