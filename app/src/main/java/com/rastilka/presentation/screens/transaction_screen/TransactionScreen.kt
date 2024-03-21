package com.rastilka.presentation.screens.transaction_screen

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rastilka.common.app_data.LoadingState
import com.rastilka.presentation.components_app.error_screen.ErrorView
import com.rastilka.presentation.screens.transaction_screen.views.CardTransaction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(viewModel: TransactionViewModel = hiltViewModel()) {

    val transactionList by viewModel.transactionList.collectAsState()
    val initLoadingState by viewModel.initLoadingState.collectAsState()
    val lazyListState = rememberLazyListState()
    val stateRefresh = rememberPullToRefreshState(
        positionalThreshold = 180.dp,
        enabled = {
            initLoadingState == LoadingState.SuccessfulLoad
        }
    )
    val scaleFraction = if (stateRefresh.isRefreshing) 1f else
        LinearOutSlowInEasing.transform(stateRefresh.progress).coerceIn(0f, 1f)

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
                        text = "Начисления",
                        fontWeight = FontWeight.Bold,
                    )
                }
            )
        },
        modifier = Modifier.nestedScroll(stateRefresh.nestedScrollConnection)
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
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = transactionList,
                        key = { transaction ->
                            transaction.id
                        }
                    ) { transaction ->
                        CardTransaction(transaction = transaction)
                    }
                }
            } else {
                ErrorView(refreshFun = {
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

    }
    if (initLoadingState == LoadingState.Loading) {
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
}