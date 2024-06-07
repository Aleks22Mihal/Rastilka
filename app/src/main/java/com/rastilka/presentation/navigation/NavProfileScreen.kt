package com.rastilka.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.rastilka.presentation.navigation.navigation_models.AppNavGraph
import com.rastilka.presentation.navigation.navigation_models.NavigationScreens
import com.rastilka.presentation.screens.edit_profile_screen.EditProfileScreen
import com.rastilka.presentation.screens.edit_profile_screen.EditProfileScreenViewModel
import com.rastilka.presentation.screens.login_screen.LoginViewModel
import com.rastilka.presentation.screens.profile_screen.ProfileScreen
import com.rastilka.presentation.screens.technical_support_screen.TechnicalSupportScreen
import com.rastilka.presentation.screens.technical_support_screen.TechnicalSupportViewModel

fun NavGraphBuilder.navProfileScreen(
    loginViewModel: LoginViewModel,
    navController: NavController
) {
    navigation(
        route = AppNavGraph.ProfileGraph.rout,
        startDestination = NavigationScreens.ProfileScreen.rout,
    ) {
        composable(route = NavigationScreens.ProfileScreen.rout) {
            ProfileScreen(
                state = loginViewModel.state.collectAsState(),
                onEvent = loginViewModel::onEvent,
                navController = navController,
            )
        }

        composable(
            route = NavigationScreens.EditProfileScreen.rout,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
            val viewModel = hiltViewModel<EditProfileScreenViewModel>()

            EditProfileScreen(
                state = viewModel.state.collectAsState(),
                onEvent = viewModel::onEvent,
                navController = navController
            )
        }

        composable(
            route = NavigationScreens.TechnicalSupportScreen.rout,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
            val viewModel = hiltViewModel<TechnicalSupportViewModel>()

            TechnicalSupportScreen(
                state = viewModel.state.collectAsState(),
                onEvent = viewModel::onEvent,
                navController = navController
            )
        }
    }
}
