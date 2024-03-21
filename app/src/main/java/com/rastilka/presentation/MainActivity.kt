package com.rastilka.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rastilka.common.app_data.LoadingState
import com.rastilka.presentation.navigation.AppNavHost
import com.rastilka.presentation.navigation.bottom_navigation_bar.BottomNavigationBar
import com.rastilka.presentation.navigation.navigation_models.NavigationScreens
import com.rastilka.presentation.screens.login_screen.LoginScreen
import com.rastilka.presentation.screens.login_screen.LoginViewModel
import com.rastilka.presentation.screens.splash_screen.SplashScreen
import com.rastilka.presentation.ui.theme.RastilkaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

            val loginViewModel: LoginViewModel = hiltViewModel()
            val user by loginViewModel.user.collectAsState()
            val loadingStateInit by loginViewModel.loadingStatusInit.collectAsState()


            RastilkaTheme(
                darkTheme = false
            ) {
                if (loadingStateInit == LoadingState.Loading) {
                    SplashScreen()
                } else {
                    if (user?.userExist == true) {
                        val navController = rememberNavController()
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        Scaffold(
                            bottomBar = {
                                AnimatedVisibility(
                                    visible = navBackStackEntry?.destination?.route != NavigationScreens.CreateTaskScreen.rout ||
                                            navBackStackEntry?.destination?.route != NavigationScreens.CreateWishScreen.rout,
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
                        LoginScreen(viewModel = loginViewModel)
                    }
                }
            }
        }
    }
}