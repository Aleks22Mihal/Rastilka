package com.rastilka.presentation.screens.profile_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rastilka.presentation.screens.login_screen.LoginViewModel
import com.rastilka.presentation.screens.profile_screen.components.ProfileMenuView
import com.rastilka.presentation.screens.profile_screen.components.UserView

@Composable
fun ProfileScreen(viewModel: LoginViewModel) {

    val user by viewModel.user.collectAsState()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        UserView(user = user)
        ProfileMenuView()
        Button(onClick = { viewModel.logOut() }) {
            Text(text = "Выход")
        }
    }
}