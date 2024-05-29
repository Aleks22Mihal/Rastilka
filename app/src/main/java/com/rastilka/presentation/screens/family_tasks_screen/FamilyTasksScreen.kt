package com.rastilka.presentation.screens.family_tasks_screen

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rastilka.R
import com.rastilka.common.app_data.LoadingState
import com.rastilka.common.utilits_support_preview.SupportPreview
import com.rastilka.presentation.components_app.error_screen.ErrorView
import com.rastilka.presentation.navigation.navigation_models.NavigationScreens
import com.rastilka.presentation.screens.family_tasks_screen.components.DialogTaskDatePickerView
import com.rastilka.presentation.screens.family_tasks_screen.components.LazyColumTasksView
import com.rastilka.presentation.screens.family_tasks_screen.components.LazyRowFamilyMembersTasksView
import com.rastilka.presentation.screens.family_tasks_screen.data.FamilyTasksScreenEvent
import com.rastilka.presentation.screens.family_tasks_screen.data.FamilyTasksScreenState
import com.rastilka.presentation.ui.theme.RastilkaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyTasksScreen(
    navController: NavController,
    state: State<FamilyTasksScreenState>,
    onEvent: (FamilyTasksScreenEvent) -> Unit
) {
    val stateRefresh = rememberPullToRefreshState(
        positionalThreshold = 180.dp,
        enabled = {
            state.value.initLoadingState == LoadingState.SuccessfulLoad
        }
    )

    val scaleFraction = if (stateRefresh.isRefreshing) {
        1f
    } else {
        LinearOutSlowInEasing.transform(stateRefresh.progress).coerceIn(0f, 1f)
    }

    if (stateRefresh.isRefreshing) {
        LaunchedEffect(true) {
            onEvent(FamilyTasksScreenEvent.Refresh)
            stateRefresh.endRefresh()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_sreen_task),
                            contentDescription = "Task"
                        )
                        Text(text = "Задачи")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(NavigationScreens.CreateTaskScreen.rout)
                        }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_add_task),
                            contentDescription = "Add task"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        modifier = Modifier.nestedScroll(stateRefresh.nestedScrollConnection)
    ) { innerPadding ->

        when (state.value.initLoadingState) {
            LoadingState.SuccessfulLoad -> {
                val selectedTaskForChangeDate = remember { mutableStateOf(Pair("", 0L)) }

                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    LazyRowFamilyMembersTasksView(
                        state = state,
                        onEvent = onEvent
                    )

                    LazyColumTasksView(
                        state = state,
                        userId = state.value.user?.id ?: "",
                        onEvent = onEvent,
                        selectedTaskForChangeDate = selectedTaskForChangeDate,
                    )
                }

                if (state.value.datePickerDialog) {
                    DialogTaskDatePickerView(
                        taskUrl = selectedTaskForChangeDate.value.first,
                        taskDateMillis = selectedTaskForChangeDate.value.second,
                        onEvent = onEvent
                    )
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

            LoadingState.FailedLoad -> {
                ErrorView(
                    textError = state.value.errorMessage,
                    refreshFun = {
                        onEvent(FamilyTasksScreenEvent.Refresh)
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

@Preview
@Composable
fun DemoFamilyTasksScreen() {
    RastilkaTheme {
        FamilyTasksScreen(
            navController = rememberNavController(),
            state = remember {
                mutableStateOf(
                    FamilyTasksScreenState(
                        initLoadingState = LoadingState.SuccessfulLoad,
                        filterTasks = listOf(SupportPreview.task),
                        familyMembers = SupportPreview.listFamily
                    )
                )
            },
            onEvent = {}
        )
    }
}
