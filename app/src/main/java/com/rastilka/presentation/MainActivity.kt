package com.rastilka.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rastilka.common.app_data.LoadingState
import com.rastilka.presentation.navigation.AppNavHost
import com.rastilka.presentation.navigation.bottom_navigation_bar.BottomNavigationBar
import com.rastilka.presentation.navigation.navigation_models.NavigationScreens.CreateTaskScreen
import com.rastilka.presentation.navigation.navigation_models.NavigationScreens.CreateWishScreen
import com.rastilka.presentation.screens.login_screen.LoginScreen
import com.rastilka.presentation.screens.login_screen.LoginViewModel
import com.rastilka.presentation.screens.splash_screen.SplashScreen
import com.rastilka.presentation.ui.theme.RastilkaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val loginViewModel: LoginViewModel = hiltViewModel()
            val state by loginViewModel.state.collectAsState()

            RastilkaTheme(
                darkTheme = false
            ) {
                if (state.initLoadingState == LoadingState.Loading) {
                    SplashScreen()
                } else {
                    if (state.user?.userExist == true) {
                        val navController = rememberNavController()
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        Scaffold(
                            bottomBar = {
                                AnimatedVisibility(
                                    visible = navBackStackEntry?.destination?.route != CreateTaskScreen.rout ||
                                            navBackStackEntry?.destination?.route != CreateWishScreen.rout,
                                ) {
                                    BottomNavigationBar(navController = navController)
                                }
                            },
                        ) { innerPadding ->
                            AppNavHost(
                                navController = navController,
                                innerPadding = innerPadding,
                                loginViewModel = loginViewModel
                            )
                        }
                    } else {
                        LoginScreen(
                            state = state,
                            onEvent = loginViewModel::onEvent
                        )
                    }
                }
            }
        }
    }
}
