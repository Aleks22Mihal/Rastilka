package com.rastilka.presentation.screens.login_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rastilka.R
import com.rastilka.common.app_data.LoadingState
import com.rastilka.presentation.components_app.error_screen.ErrorView
import com.rastilka.presentation.screens.login_screen.views.LoginView
import com.rastilka.presentation.screens.login_screen.views.RegistrationView

@Composable
fun LoginScreen(viewModel: LoginViewModel) {

    val loadingState by viewModel.loadingState.collectAsState()
    val userStatus by viewModel.userStatus.collectAsState()
    val focusManager = LocalFocusManager.current
    var isLogin by remember { mutableStateOf(true) }

    if (loadingState != LoadingState.FailedLoad) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .height(IntrinsicSize.Max)
                .imePadding()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                        }
                    )
                }
        ) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                ElevatedCard(
                    elevation = CardDefaults.elevatedCardElevation(5.dp),
                    shape = RoundedCornerShape(topEnd = 50.dp, topStart = 50.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 120.dp),
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(top = 80.dp, start = 30.dp, end = 30.dp)
                    ) {
                        Text(
                            text = "Добро пожаловать в Растилку",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 5.dp)
                        )
                        Text(
                            text = if (isLogin) "Войдите в Ваш аккаунт" else "Создайте свой аккаут",
                            fontWeight = FontWeight.Light,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        when (isLogin) {
                            true -> {
                                LoginView(
                                    viewModel = viewModel,
                                    userStatus = userStatus,
                                )
                            }

                            false -> {
                                RegistrationView(viewModel = viewModel)
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = if (isLogin) "У Вас нет аккаунта?" else "У Вас есть аккаунта?")
                            TextButton(onClick = {
                                viewModel.userStatusNull()
                                isLogin = !isLogin
                            }) {
                                Text(text = if (isLogin) "Создать" else "Войти")
                            }
                        }
                    }
                }
                ElevatedCard(
                    elevation = CardDefaults.elevatedCardElevation(5.dp),
                    shape = RoundedCornerShape(100),
                    modifier = Modifier.padding(60.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_rastilka),
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .padding(15.dp)

                    )
                }
            }
        }
        if (loadingState == LoadingState.Loading) {
            AlertDialog(
                modifier = Modifier.size(100.dp),
                onDismissRequest = {},
                confirmButton = {},
                text = {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            )
        }
    } else {
        ErrorView(refreshFun = {
            viewModel.init()
        }
        )
    }
}