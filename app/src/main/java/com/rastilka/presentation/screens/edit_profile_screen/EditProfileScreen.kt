package com.rastilka.presentation.screens.edit_profile_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rastilka.R
import com.rastilka.common.app_data.LoadingState
import com.rastilka.common.utilits_support_preview.SupportPreview
import com.rastilka.presentation.components_app.error_screen.ErrorView
import com.rastilka.presentation.screens.edit_profile_screen.component.EditPhotoView
import com.rastilka.presentation.screens.edit_profile_screen.component.EditProfileAttributesView
import com.rastilka.presentation.screens.edit_profile_screen.data.EditProfileScreenEvent
import com.rastilka.presentation.screens.edit_profile_screen.data.EditProfileScreenState
import com.rastilka.presentation.ui.theme.RastilkaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    state: State<EditProfileScreenState>,
    onEvent: (EditProfileScreenEvent) -> Unit,
    navController: NavController,
) {

    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Аккаунт")
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
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    focusManager.clearFocus()
                }
            )
        }
    ) { scaffoldInnerPadding ->

        when (state.value.initLoadingState) {

            LoadingState.SuccessfulLoad -> {

                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        space = 8.dp,
                        alignment = Alignment.Top
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface
                        )
                        .padding(scaffoldInnerPadding)
                        .fillMaxSize()
                        .imePadding()
                        .verticalScroll(rememberScrollState())
                ) {
                    EditPhotoView(state = state, onEvent = onEvent)
                    EditProfileAttributesView(state = state, onEvent = onEvent)

                    Button(
                        onClick = {
                            onEvent(EditProfileScreenEvent.EditUserAndPassword)
                        },
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        contentPadding = PaddingValues(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Сохранить изменения",
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (state.value.loadingState == LoadingState.Loading) {
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
            }

            LoadingState.FailedLoad -> {
                ErrorView(
                    refreshFun = {
                        onEvent(EditProfileScreenEvent.Refresh)
                    },
                    textError = state.value.screenErrorMessage
                )
            }

            LoadingState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(scaffoldInnerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview
@Composable
private fun DemoEditProfileScreen() {
    RastilkaTheme {
        EditProfileScreen(
            navController = rememberNavController(),
            state = remember {
                mutableStateOf(
                    EditProfileScreenState(
                        user = SupportPreview.user,
                        editPhoto = "1"
                    )
                )
            },
            onEvent = {}
        )
    }
}