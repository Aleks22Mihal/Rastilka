package com.rastilka.presentation.screens.family_screen

import androidx.camera.core.ExperimentalGetImage
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rastilka.R
import com.rastilka.common.app_data.LoadingState
import com.rastilka.presentation.components_app.error_screen.ErrorView
import com.rastilka.presentation.components_app.shimmer_brush.ShimmerCardFriend
import com.rastilka.presentation.components_app.shimmer_brush.animatedShimmer
import com.rastilka.presentation.screens.family_screen.views.CardFamilyMember
import com.rastilka.presentation.screens.family_screen.views.ModalBottomQrCodeSheet

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyScreen(
    viewModel: FamilyViewModel = hiltViewModel(),
) {

    val familyList by viewModel.familyList.collectAsState()
    val initLoadingState by viewModel.initLoadingState.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    val scrollListState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showBottomSheet = remember { mutableStateOf(false) }
    val stateRefresh = rememberPullToRefreshState(
        positionalThreshold = 180.dp,
        enabled = {
            initLoadingState == LoadingState.SuccessfulLoad
        }
    )
    val scaleFraction = if (stateRefresh.isRefreshing) 1f else
        LinearOutSlowInEasing.transform(stateRefresh.progress).coerceIn(0f, 1f)

    val focusManager = LocalFocusManager.current

    if (stateRefresh.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.init()
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
                    Text(
                        text = "Моя семья",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            showBottomSheet.value = true
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_qr_code_scanner_24),
                            tint = MaterialTheme.colorScheme.tertiary,
                            contentDescription = null
                        )
                    }
                },
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
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            if (initLoadingState != LoadingState.FailedLoad) {
                LazyColumn(
                    contentPadding = PaddingValues(10.dp),
                    state = scrollListState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (initLoadingState == LoadingState.Loading) {
                        items(5) {
                            ShimmerCardFriend(brush = animatedShimmer())
                        }
                    } else {
                        itemsIndexed(
                            items = familyList,
                            key = { _, familyMember ->
                                familyMember.id
                            }
                        ) { _, familyMember ->
                            CardFamilyMember(
                                familyMember = familyMember,
                                deleteMemberFamily = {
                                    viewModel.deleteMemberFamily(
                                        userOneId = familyList[0].id,
                                        userTwoId = familyMember.id
                                    )
                                },
                                sendPointMemberFamily = { point ->
                                    viewModel.addUserPoint(
                                        toUserId = familyMember.id,
                                        point = point
                                    )
                                },
                                getPointMemberFamily = { point ->
                                    viewModel.getUserPoint(
                                        fromUserId = familyMember.id,
                                        point = point
                                    )
                                }
                            )
                        }
                    }
                }
            } else {
                ErrorView(
                    refreshFun = {
                        viewModel.init()
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

        if (showBottomSheet.value && initLoadingState != LoadingState.FailedLoad) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet.value = false
                },
                sheetState = sheetState,
                shape = RoundedCornerShape(0)
            ) {
                ModalBottomQrCodeSheet(
                    textId = familyList[0].id,
                    showBottomSheet = showBottomSheet,
                    sheetState = sheetState,
                    viewModel = viewModel
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
}