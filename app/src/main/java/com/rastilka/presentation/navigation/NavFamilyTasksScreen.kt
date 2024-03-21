package com.rastilka.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.rastilka.presentation.navigation.navigation_models.AppNavGraph
import com.rastilka.presentation.navigation.navigation_models.NavigationScreens
import com.rastilka.presentation.screens.create_task_screen.CreateTaskScreen
import com.rastilka.presentation.screens.family_tasks_screen.FamilyTasksScreen

fun NavGraphBuilder.navFamilyTasksScreen(
    navController: NavController
) {
    navigation(
        route = AppNavGraph.FamilyTasksGraph.rout,
        startDestination = NavigationScreens.FamilyTasksScreen.rout
    ) {
        composable(route = NavigationScreens.FamilyTasksScreen.rout) {
            FamilyTasksScreen(navController = navController)
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
            CreateTaskScreen(navController = navController)
        }
    }
}