package com.rastilka.presentation.screens.family_tasks_screen

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rastilka.R
import com.rastilka.common.app_data.LoadingState
import com.rastilka.presentation.components_app.app_components.FamilyMemberView
import com.rastilka.presentation.components_app.error_screen.ErrorView
import com.rastilka.presentation.navigation.navigation_models.NavigationScreens
import com.rastilka.presentation.screens.family_tasks_screen.components.CardTaskView
import com.rastilka.presentation.screens.family_tasks_screen.data.FamilyTasksScreenEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyTasksScreen(
    viewModel: FamilyTasksViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    val stateRefresh = rememberPullToRefreshState(
        positionalThreshold = 180.dp,
        enabled = {
            state.initLoadingState == LoadingState.SuccessfulLoad
        }
    )

    val scaleFraction = if (stateRefresh.isRefreshing) 1f else
        LinearOutSlowInEasing.transform(stateRefresh.progress).coerceIn(0f, 1f)

    if (stateRefresh.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.onEvent(FamilyTasksScreenEvent.Refresh)
            stateRefresh.endRefresh()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Задачи",
                        fontWeight = FontWeight.Bold,
                    )
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(NavigationScreens.CreateTaskScreen.rout)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_note_add_24),
                            contentDescription = ""
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
        when (state.initLoadingState) {
            LoadingState.SuccessfulLoad -> {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    LazyRow(
                        contentPadding = PaddingValues(start = 10.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .height(80.dp)
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.background)

                    ) {
                        items(state.familyMembers) { familyMember ->
                            FamilyMemberView(
                                familyMember = familyMember,
                                isSelected = familyMember.id in state.mainFolderTask?.uuid!!.forUsers,
                                size = 60.dp,
                                isVisibleNameUser = false,
                                select = {
                                    viewModel.onEvent(
                                        FamilyTasksScreenEvent.GetFilteredTasksByFamilyMembers(
                                            userId = familyMember.id
                                        )
                                    )
                                },
                            )
                        }
                    }
                    LazyColumn {
                        items(
                            items = state.filterTasks,
                            key = { it.value.id }
                        ) { task ->
                            CardTaskView(
                                task = task,
                                familyMembers = state.familyMembers,
                                delete = { productUrl ->
                                    viewModel.onEvent(
                                        FamilyTasksScreenEvent.DeleteTask(
                                            productUrl
                                        )
                                    )
                                },
                                didResponsibleUser = { userId ->
                                    viewModel.onEvent(
                                        FamilyTasksScreenEvent.SetDidResponsibleUser(
                                            productUrl = task.value.url,
                                            userId = userId,
                                        )
                                    )
                                },
                                setResponsibleUser = { userId ->
                                    viewModel.onEvent(
                                        FamilyTasksScreenEvent.AddResponsibleUser(
                                            productUrl = task.value.url,
                                            userId = userId
                                        )
                                    )
                                },
                                editTask = { property, value ->
                                    viewModel.onEvent(
                                        FamilyTasksScreenEvent.EditTask(
                                            productUrl = task.value.url,
                                            property = property,
                                            value = value
                                        )
                                    )
                                },
                                sendPoint = { usersId, points, title ->
                                    viewModel.onEvent(
                                        FamilyTasksScreenEvent.SendPoint(
                                            usersId = usersId,
                                            points = points,
                                            productUrl = task.value.url,
                                            title = title
                                        )
                                    )
                                }
                            )
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

            LoadingState.FailedLoad -> {
                ErrorView(
                    textError = state.errorMessage,
                    refreshFun = {
                        viewModel.onEvent(FamilyTasksScreenEvent.Refresh)
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