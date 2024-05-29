package com.rastilka.presentation.screens.family_screen

import androidx.camera.core.ExperimentalGetImage
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rastilka.R
import com.rastilka.common.app_data.LoadingState
import com.rastilka.presentation.components_app.error_screen.ErrorView
import com.rastilka.presentation.screens.family_screen.componets.CardFamilyMember
import com.rastilka.presentation.screens.family_screen.componets.ModalBottomQrCodeSheet
import com.rastilka.presentation.screens.family_screen.data.FamilyScreenEvent
import com.rastilka.presentation.screens.family_screen.data.FamilyScreenState

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyScreen(
    state: State<FamilyScreenState>,
    onEvent: (FamilyScreenEvent) -> Unit
) {

    val stateRefresh = rememberPullToRefreshState(
        positionalThreshold = 180.dp,
        enabled = {
            state.value.initLoadingState == LoadingState.SuccessfulLoad
        }
    )
    val scaleFraction = if (stateRefresh.isRefreshing) 1f else
        LinearOutSlowInEasing.transform(stateRefresh.progress).coerceIn(0f, 1f)

    val focusManager = LocalFocusManager.current

    if (stateRefresh.isRefreshing) {
        LaunchedEffect(true) {
            onEvent(FamilyScreenEvent.Refresh)
            stateRefresh.endRefresh()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_screen_home),
                            contentDescription = "Task",
                            modifier = Modifier.size(26.dp)
                        )
                        Text(text = "Семья")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onEvent(FamilyScreenEvent.OpenBottomSheet(true))
                        },
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_qrcode),
                            contentDescription = null,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                },
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            bottomEnd = 12.dp,
                            bottomStart = 12.dp
                        )
                    )
            )
        },
        modifier = Modifier
            .nestedScroll(stateRefresh.nestedScrollConnection)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            }
    ) { innerPadding ->

        when (state.value.initLoadingState) {

            LoadingState.SuccessfulLoad -> {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {

                    val scrollListState = rememberLazyListState()

                    LazyColumn(
                        contentPadding = PaddingValues(top = 4.dp, bottom = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        state = scrollListState,
                        modifier = Modifier
                            .fillMaxSize()
                            .imePadding()
                    ) {
                        itemsIndexed(
                            items = state.value.familyList,
                            key = { _, familyMember ->
                                familyMember.id
                            }
                        ) { _, familyMember ->
                            CardFamilyMember(
                                familyMember = familyMember,
                                userId = state.value.user?.id ?: "",
                                onEvent = onEvent,
                            )
                        }
                    }

                    if (state.value.isOpenBottomSheet) {

                        val sheetState =
                            rememberModalBottomSheetState(skipPartiallyExpanded = true)

                        ModalBottomSheet(
                            onDismissRequest = {
                                onEvent(FamilyScreenEvent.OpenBottomSheet(false))
                            },
                            containerColor = MaterialTheme.colorScheme.background,
                            sheetState = sheetState,
                            shape = RoundedCornerShape(0)
                        ) {
                            ModalBottomQrCodeSheet(
                                textId = state.value.familyList[0].id,
                                sheetState = sheetState,
                                onEvent = onEvent,
                            )
                        }
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
                Box(modifier = Modifier.fillMaxWidth()) {
                    PullToRefreshContainer(
                        state = stateRefresh,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .graphicsLayer(scaleX = scaleFraction, scaleY = scaleFraction),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                }

                if (state.value.isOpenErrorDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            onEvent(FamilyScreenEvent.OpenAndCloseErrorDialog(false))
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                onEvent(FamilyScreenEvent.OpenAndCloseErrorDialog(false))
                            }) {
                                Text(text = "Ok")
                            }
                        },
                        text = {
                            Text(
                                text = state.value.errorText ?: "",
                                textAlign = TextAlign.Center,
                                fontSize = 24.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    )
                }
            }

            LoadingState.FailedLoad -> {
                ErrorView(
                    refreshFun = {
                        onEvent(FamilyScreenEvent.Refresh)
                    }
                )
            }

            LoadingState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}