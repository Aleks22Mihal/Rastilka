package com.rastilka.presentation.screens.family_wishes_screen

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rastilka.R
import com.rastilka.common.app_data.LoadingState
import com.rastilka.presentation.components_app.error_screen.ErrorView
import com.rastilka.presentation.navigation.navigation_models.NavigationScreens
import com.rastilka.presentation.screens.family_wishes_screen.components.CardWishView
import com.rastilka.presentation.screens.family_wishes_screen.data.FamilyWishScreenEvent
import com.rastilka.presentation.screens.family_wishes_screen.data.FamilyWishScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyWishesScreen(
    state: State<FamilyWishScreenState>,
    onEvent: (FamilyWishScreenEvent) -> Unit,
    navController: NavController
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
            onEvent(FamilyWishScreenEvent.Refresh)
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
                            painter = painterResource(id = R.drawable.ic_wish),
                            contentDescription = "Task"
                        )
                        Text(text = "Цели")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(NavigationScreens.CreateWishScreen.rout)
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_add_task),
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
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = PaddingValues(top = 4.dp, bottom = 4.dp),
                        modifier = Modifier.fillMaxSize().imePadding(),
                    ) {
                        items(state.value.wishList) { wish ->
                            CardWishView(
                                wish = wish,
                                listFamilyMembers = state.value.familyMembers,
                                onEvent = onEvent
                            )
                        }
                    }
                }
                if (state.value.loadingState == LoadingState.Loading) {
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
            }

            LoadingState.FailedLoad -> {
                ErrorView(
                    textError = state.value.errorMessage,
                    refreshFun = {
                        onEvent(FamilyWishScreenEvent.Refresh)
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