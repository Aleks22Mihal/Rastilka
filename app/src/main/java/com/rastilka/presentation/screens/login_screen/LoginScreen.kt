package com.rastilka.presentation.screens.login_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rastilka.R
import com.rastilka.common.app_data.LoadingState
import com.rastilka.presentation.components_app.error_screen.ErrorView
import com.rastilka.presentation.screens.login_screen.components.ForgetPasswordDialogView
import com.rastilka.presentation.screens.login_screen.components.LoginView
import com.rastilka.presentation.screens.login_screen.components.RegistrationView
import com.rastilka.presentation.screens.login_screen.data.LoginScreenEvent
import com.rastilka.presentation.screens.login_screen.data.LoginScreenState
import com.rastilka.presentation.ui.theme.RastilkaTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginScreen(
    state: LoginScreenState,
    onEvent: (LoginScreenEvent) -> Unit,
) {

    if (state.initLoadingState != LoadingState.FailedLoad) {

        val countyPages = 2
        val horizontalPageState = rememberPagerState { countyPages }
        val scope = rememberCoroutineScope()
        val snackBarHostState = remember { SnackbarHostState() }


        if (state.isSnackBarShowing) {
            val textSnackBar = if (state.loadingStateForgetPassword ==
                LoadingState.SuccessfulLoad
            ) {
                "Письмо отправленно на почту"
            } else {
                "Что-то пошло не так"
            }
            LaunchedEffect(true) {
                snackBarHostState.showSnackbar(textSnackBar)
            }
        }

        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = snackBarHostState,
                    modifier = Modifier
                        .padding(10.dp)
                ) { data ->
                    Snackbar(
                        snackbarData = data,
                    )
                }
            }
        ) { innerPadding ->

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.surface)
                    .verticalScroll(rememberScrollState())
                    .imePadding()
            ) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                        )
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(30.dp))
                    Image(
                        painter = painterResource(id = R.drawable.logo_rastilka),
                        contentDescription = null,
                        modifier = Modifier
                            .size(150.dp)
                            .padding(15.dp)
                    )
                    Text(
                        text = "Добро пожаловать в Растилку",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                }

                HorizontalPager(
                    state = horizontalPageState,
                    userScrollEnabled = false,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxSize()
                ) { page ->
                    when (page) {
                        0 -> {
                            LoginView(
                                state = state,
                                onEvent = onEvent,
                                registrationOnClick = {
                                    scope.launch { horizontalPageState.animateScrollToPage(1) }
                                }
                            )
                        }

                        1 -> {
                            RegistrationView(
                                state = state,
                                onEvent = onEvent,
                                loginOnClick = {
                                    scope.launch { horizontalPageState.animateScrollToPage(0) }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (horizontalPageState.currentPage == 0) {
                            "У Вас нет аккаунта?"
                        } else {
                            "У Вас есть аккаунт?"
                        }

                    )
                    TextButton(onClick = {

                        scope.launch {
                            if (horizontalPageState.currentPage == 0) {
                                horizontalPageState.animateScrollToPage(1)
                            } else {
                                horizontalPageState.animateScrollToPage(0)
                            }
                        }

                    }) {
                        Text(
                            text = if (horizontalPageState.currentPage == 0) {
                                "Создать"
                            } else {
                                "Войти"
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

            }

            if (state.isVisibleForgetPasswordDialog) {
                ForgetPasswordDialogView(
                    state = state,
                    onEvent = onEvent,
                )
            }

            if (state.loadingState == LoadingState.Loading) {
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
        }
    } else {
        ErrorView(
            refreshFun = {
                onEvent(LoginScreenEvent.Refresh)
            }
        )
    }
}

@Preview
@Composable
private fun DemoLoginScreen() {
    RastilkaTheme {
        LoginScreen(
            state = LoginScreenState(),
            onEvent = {}
        )
    }
}
