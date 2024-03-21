package com.rastilka.presentation.screens.family_wishes_screen

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavController
import com.rastilka.R
import com.rastilka.common.app_data.LoadingState
import com.rastilka.presentation.navigation.navigation_models.NavigationScreens
import com.rastilka.presentation.screens.family_wishes_screen.data.FamilyWishScreenEvent
import com.rastilka.presentation.screens.family_wishes_screen.views.CardWishView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyWishesScreen(viewModel: FamilyWishesViewModel = hiltViewModel(), navController: NavController) {

    val state by viewModel.state.collectAsState()

    val stateRefresh = rememberPullToRefreshState(
        positionalThreshold = 180.dp,
        enabled = {
            state.initLoadingState == LoadingState.SuccessfulLoad
        }
    )
    val scaleFraction = if (stateRefresh.isRefreshing) 1f else
        LinearOutSlowInEasing.transform(stateRefresh.progress).coerceIn(0f, 1f)

    val focusManager = LocalFocusManager.current

    if (stateRefresh.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.onEvent(FamilyWishScreenEvent.Refresh)
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
                        text = "Семейные дела",
                        fontWeight = FontWeight.Bold,
                    )
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(NavigationScreens.CreateWishScreen.rout)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_note_add_24),
                            contentDescription = "Add Wish"
                        )
                    }
                }
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
            if (state.initLoadingState == LoadingState.SuccessfulLoad) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    items(state.wishList) { wish ->
                        CardWishView(
                            wish = wish,
                            listFamilyMembers = state.familyMembers,
                            addResponsibleUser = { familyMemberId, activeUserId ->
                                viewModel.onEvent(
                                    FamilyWishScreenEvent.AddResponsibleUser(
                                        productUrl = wish.value.url,
                                        userId = familyMemberId,
                                        activeUserId = activeUserId
                                    )
                                )
                            },
                            getPoints = { points ->
                                viewModel.onEvent(
                                    FamilyWishScreenEvent.GetPoint(
                                        fromUserId = wish.uuid.forUsers.first(),
                                        points = points,
                                        comment = wish.value.h1,
                                        productUrl = wish.value.url,
                                        assembly = wish.value.assembly ?: "0"
                                    )
                                )
                            },
                            deleteMemberFamily = {
                                viewModel.onEvent(FamilyWishScreenEvent.DeleteWish(wish.value.url))
                            }
                        )
                    }
                }
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        if (state.loadingState == LoadingState.Loading) {
            AlertDialog(
                modifier = Modifier.size(100.dp),
                onDismissRequest = {},
                confirmButton = {},
                text = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.padding(8.dp))
                    }
                }
            )
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
    }
}