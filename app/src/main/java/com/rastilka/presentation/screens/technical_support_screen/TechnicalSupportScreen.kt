package com.rastilka.presentation.screens.technical_support_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rastilka.R
import com.rastilka.common.app_data.LoadingState
import com.rastilka.presentation.components_app.clean_focus_keyboard.clearFocusOnKeyboardDismiss
import com.rastilka.presentation.components_app.error_screen.ErrorView
import com.rastilka.presentation.screens.technical_support_screen.components.TechnicalChatBottomSendView
import com.rastilka.presentation.screens.technical_support_screen.components.TechnicalChatBubbleView
import com.rastilka.presentation.screens.technical_support_screen.data.TechnicalSupportEvent
import com.rastilka.presentation.screens.technical_support_screen.data.TechnicalSupportState
import com.rastilka.presentation.ui.theme.RastilkaTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechnicalSupportScreen(
    state: State<TechnicalSupportState>,
    onEvent: (TechnicalSupportEvent) -> Unit,
    navController: NavController,
    serverRequestTimer: Long = 5000,
) {

    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Техничкская поддержка")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back_24),
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            TechnicalChatBottomSendView(
                state = state,
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                    )
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .clearFocusOnKeyboardDismiss()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            }
    ) { scaffoldInnerPadding ->
        when (state.value.initLoadingState) {
            LoadingState.SuccessfulLoad -> {

                val lazyColumState = rememberLazyListState()

                LaunchedEffect(key1 = state.value.listMessage.isNotEmpty()) {
                    while (true) {
                        delay(serverRequestTimer)
                        onEvent(TechnicalSupportEvent.GetMessage)
                    }
                }

                LazyColumn(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    reverseLayout = true,
                    state = lazyColumState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(scaffoldInnerPadding)
                ) {
                    items(state.value.listMessage) { message ->
                        TechnicalChatBubbleView(
                            message = message,
                            userName = state.value.user?.name ?: "",
                        )
                    }
                }
            }

            LoadingState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            LoadingState.FailedLoad -> {
                ErrorView(
                    refreshFun = {
                        onEvent(TechnicalSupportEvent.Refresh)
                    },
                    textError = state.value.errorMessage
                )
            }
        }
    }
}

@Preview
@Composable
private fun DemoTechnicalSupportScreen() {
    RastilkaTheme {
        TechnicalSupportScreen(
            state = remember {
                mutableStateOf(TechnicalSupportState())
            },
            onEvent = {},
            navController = rememberNavController()
        )
    }
}