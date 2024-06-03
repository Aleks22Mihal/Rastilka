package com.rastilka.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rastilka.presentation.navigation.navigation_models.AppNavGraph
import com.rastilka.presentation.navigation.navigation_models.NavigationScreens
import com.rastilka.presentation.screens.create_task_screen.CreateTaskScreen
import com.rastilka.presentation.screens.create_task_screen.CreateTaskViewModel
import com.rastilka.presentation.screens.family_tasks_screen.FamilyTasksScreen
import com.rastilka.presentation.screens.family_tasks_screen.FamilyTasksViewModel

fun NavGraphBuilder.navFamilyTasksScreen(
    navController: NavController
) {
    navigation(
        route = AppNavGraph.FamilyTasksGraph.rout,
        startDestination = NavigationScreens.FamilyTasksScreen.rout
    ) {
        composable(route = NavigationScreens.FamilyTasksScreen.rout) {

            val viewModel = hiltViewModel<FamilyTasksViewModel>()

            FamilyTasksScreen(
                navController = navController,
                state = viewModel.state.collectAsState(),
                onEvent = viewModel::onEvent,
            )
        }

        composable(
            route = NavigationScreens.CreateTaskScreen.rout,
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

            val viewModel = hiltViewModel<CreateTaskViewModel>()

            CreateTaskScreen(
                state = viewModel.state.collectAsState(),
                onEvent = viewModel::onEvent,
                navController = navController
            )
        }
    }
}
