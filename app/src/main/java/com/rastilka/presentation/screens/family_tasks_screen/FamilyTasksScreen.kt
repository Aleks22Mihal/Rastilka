package com.rastilka.presentation.screens.family_tasks_screen

import android.os.Build
import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
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
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyColumnState
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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

                val view = LocalView.current

                val lazyListState = rememberLazyListState()

                val datePickerState = rememberDatePickerState(
                    yearRange = LocalDate.now().year..DatePickerDefaults.YearRange.last,
                    selectableDates = object : SelectableDates {
                        override fun isSelectableDate(utcTimeMillis: Long): Boolean {

                            val now = LocalDateTime.now().minusDays(1)
                            val parForConSecToMillisecond = 1000
                            val nowInSeconds =
                                now.toEpochSecond(ZoneOffset.UTC) * parForConSecToMillisecond

                            return utcTimeMillis >= nowInSeconds
                        }

                        override fun isSelectableYear(year: Int): Boolean {
                            return year >= LocalDate.now().year
                        }
                    }
                )

                val selectedTaskForChangeDateUrl = remember { mutableStateOf("") }

                var indexFrom by remember { mutableIntStateOf(0) }
                var indexTo by remember { mutableIntStateOf(0) }
                var indexStart by remember { mutableIntStateOf(0) }
                val reorderableLazyColumnState =
                    rememberReorderableLazyColumnState(lazyListState) { from, to ->
                        indexTo = to.index
                        indexFrom = from.index
                        Log.e("", "indexFrom: $indexFrom, indexTo: $indexTo")
                        viewModel.onEvent(
                            FamilyTasksScreenEvent.MoveItemInList(indexFrom, indexTo)
                        )
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                            view.performHapticFeedback(
                                HapticFeedbackConstants.SEGMENT_FREQUENT_TICK
                            )
                        }
                    }

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
                        item {
                            IconButton(
                                onClick = {
                                    viewModel.onEvent(
                                        FamilyTasksScreenEvent.ChangeFilterDate(
                                            state = !state.filterDateNow
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .padding(end = 4.dp)
                                    .border(
                                        width = 4.dp,
                                        shape = MaterialTheme.shapes.extraLarge,
                                        color = if (state.filterDateNow) {
                                            Color.Transparent
                                        } else MaterialTheme.colorScheme.primary
                                    )
                                    .size(60.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_access_time_24),
                                    contentDescription = "Filter Time",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        items(state.familyMembers) { familyMember ->
                            FamilyMemberView(
                                familyMember = familyMember,
                                isSelected = familyMember.id == state.filterUserId,
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
                    LazyColumn(
                        state = lazyListState,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = PaddingValues(top = 4.dp, bottom = 4.dp),
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        itemsIndexed(
                            items = state.filterTasks,
                            key = { _, task ->
                                task.value.id
                            }
                        ) { index, task ->
                            ReorderableItem(
                                reorderableLazyListState = reorderableLazyColumnState,
                                key = task.value.id,
                            ) { isDragging ->

                                val elevation by animateDpAsState(
                                    if (isDragging) 4.dp else 0.dp,
                                    label = "Elevation"
                                )

                                CardTaskView(
                                    task = task,
                                    familyMembers = state.familyMembers,
                                    onEvent = viewModel::onEvent,
                                    selectedTaskForChangeDateUrl = selectedTaskForChangeDateUrl,
                                    datePikerState = datePickerState,
                                    modifier = Modifier
                                        .longPressDraggableHandle(
                                            onDragStarted = {
                                                indexStart = index
                                                Log.e("indexStart", "$indexStart")
                                                if (Build.VERSION.SDK_INT >=
                                                    Build.VERSION_CODES.UPSIDE_DOWN_CAKE
                                                ) {
                                                    view.performHapticFeedback(
                                                        HapticFeedbackConstants.DRAG_START
                                                    )
                                                } else {
                                                    view.performHapticFeedback(
                                                        HapticFeedbackConstants.VIRTUAL_KEY
                                                    )
                                                }
                                            },
                                            onDragStopped = {
                                                if (Build.VERSION.SDK_INT >=
                                                    Build.VERSION_CODES.R
                                                ) {
                                                    view.performHapticFeedback(
                                                        HapticFeedbackConstants.GESTURE_END
                                                    )
                                                } else {
                                                    view.performHapticFeedback(
                                                        HapticFeedbackConstants.VIRTUAL_KEY
                                                    )
                                                }
                                                if (indexTo != indexFrom) {
                                                    Log.e("indexStart_End", "emd: $indexStart")
                                                    viewModel.onEvent(
                                                        FamilyTasksScreenEvent
                                                            .ChangeLocationItemInList(
                                                                urlFrom = state.filterTasks[indexTo].value.url,
                                                                urlTo = state.filterTasks[indexFrom].value.url
                                                            )
                                                    )
                                                    indexFrom = 0
                                                    indexTo = 0
                                                }
                                            },
                                        )
                                        .shadow(elevation)
                                )
                            }
                        }
                    }
                }

                if (state.datePickerDialog) {
                    DatePickerDialog(
                        shape = MaterialTheme.shapes.extraSmall,
                        onDismissRequest = {
                            viewModel
                                .onEvent(FamilyTasksScreenEvent.ChangeStateDateDialog(false))
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                if (datePickerState.selectedDateMillis != null) {

                                    val apiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                    val instant =
                                        Instant.ofEpochMilli(datePickerState.selectedDateMillis!!)
                                    val date =
                                        LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                                    val dateFormatter = apiFormat.format(date)

                                    viewModel.onEvent(
                                        FamilyTasksScreenEvent.EditTask(
                                            productUrl = selectedTaskForChangeDateUrl.value,
                                            property = "date",
                                            value = dateFormatter
                                        )
                                    )

                                    viewModel.onEvent(
                                        FamilyTasksScreenEvent.ChangeStateDateDialog(false)
                                    )
                                } else {
                                    viewModel.onEvent(
                                        FamilyTasksScreenEvent.ChangeStateDateDialog(false)
                                    )
                                }
                            }) {
                                Text(text = "Изменить")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
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