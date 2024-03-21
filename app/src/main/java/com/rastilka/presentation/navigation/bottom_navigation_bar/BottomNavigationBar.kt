package com.rastilka.presentation.navigation.bottom_navigation_bar

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rastilka.presentation.navigation.bottom_navigation_bar.data.NavigationBarItem

@Composable
fun BottomNavigationBar(navController: NavController) {

    val navigationButtons = listOf(
        NavigationBarItem.FriendsScreen,
        NavigationBarItem.TasksScreen,
        NavigationBarItem.GoalsScreen,
        NavigationBarItem.ScoresScreen,
        NavigationBarItem.ProfileScreen
    )
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
    ){
        navigationButtons.forEach { navigationButtons ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Black
                ),
                icon = {
                    Icon(
                        painter = painterResource(id = navigationButtons.icon),
                        contentDescription = navigationButtons.route
                    )
                },
                selected = currentDestination?.hierarchy?.any {
                    it.route == navigationButtons.route
                } == true,
                alwaysShowLabel = false,
                onClick = {
                    navController.navigate(route = navigationButtons.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}