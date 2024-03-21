package com.rastilka.presentation.navigation.bottom_navigation_bar.data

import androidx.annotation.DrawableRes
import com.rastilka.R
import com.rastilka.presentation.navigation.navigation_models.AppNavGraph

sealed class NavigationBarItem(
    val route: String,
    @DrawableRes val icon: Int,
    @DrawableRes val iconLine: Int?
) {
    data object FriendsScreen : NavigationBarItem(
        route = AppNavGraph.FamilyGraph.rout,
        icon = R.drawable.ic_family_24,
        iconLine = null
    )
    data object TasksScreen : NavigationBarItem(
        route = AppNavGraph.FamilyTasksGraph.rout,
        icon = R.drawable.ic_task_24,
        iconLine = null
    )
    data object GoalsScreen : NavigationBarItem(
        route = AppNavGraph.WishesGraph.rout,
        icon = R.drawable.ic_goal_24,
        iconLine = null
    )
    data object ScoresScreen : NavigationBarItem(
        route = AppNavGraph.ScoresGraph.rout,
        icon = R.drawable.ic_score_24,
        iconLine = null
    )
    data object ProfileScreen : NavigationBarItem(
        route = AppNavGraph.ProfileGraph.rout,
        icon = R.drawable.ic_profile_24,
        iconLine = null
    )
}