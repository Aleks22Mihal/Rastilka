package com.rastilka.presentation.screens.transaction_screen

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rastilka.R
import com.rastilka.common.app_data.LoadingState
import com.rastilka.presentation.components_app.error_screen.ErrorView
import com.rastilka.presentation.screens.transaction_screen.data.TransactionScreenEvent
import com.rastilka.presentation.screens.transaction_screen.data.TransactionScreenState
import com.rastilka.presentation.screens.transaction_screen.views.CardTransaction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    state: State<TransactionScreenState>,
    onEvent: (TransactionScreenEvent) -> Unit
) {

    val lazyListState = rememberLazyListState()
    val stateRefresh = rememberPullToRefreshState(
        positionalThreshold = 180.dp,
        enabled = {
            state.value.initLoadingState == LoadingState.SuccessfulLoad
        }
    )
    val scaleFraction = if (stateRefresh.isRefreshing) 1f else
        LinearOutSlowInEasing.transform(stateRefresh.progress).coerceIn(0f, 1f)

    if (stateRefresh.isRefreshing) {
        LaunchedEffect(true) {
            onEvent(TransactionScreenEvent.Refresh)
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
                            painter = painterResource(id = R.drawable.ic_screen_transaction),
                            contentDescription = "Task",
                            modifier = Modifier.size(26.dp)
                        )
                        Text(text = "Начисления")
                    }
                }
            )
        },
        modifier = Modifier.nestedScroll(stateRefresh.nestedScrollConnection)
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
                        contentPadding = PaddingValues(top = 4.dp, bottom = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        state = lazyListState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = state.value.transactionList,
                            key = { transaction ->
                                transaction.id
                            }
                        ) { transaction ->
                            CardTransaction(transaction = transaction)
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

            LoadingState.Loading -> {
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

            LoadingState.FailedLoad -> {
                ErrorView(refreshFun = {
                    onEvent(TransactionScreenEvent.Refresh)
                }
                )
            }
        }
    }
}