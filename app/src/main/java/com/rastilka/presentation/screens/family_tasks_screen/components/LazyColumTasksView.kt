package com.rastilka.presentation.screens.family_tasks_screen.components

import android.os.Build
import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rastilka.common.utilits_support_preview.SupportPreview
import com.rastilka.presentation.screens.family_tasks_screen.data.FamilyTasksScreenEvent
import com.rastilka.presentation.screens.family_tasks_screen.data.FamilyTasksScreenState
import com.rastilka.presentation.ui.theme.RastilkaTheme
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyColumnState

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun LazyColumTasksView(
    state: State<FamilyTasksScreenState>,
    onEvent: (FamilyTasksScreenEvent) -> Unit,
    selectedTaskForChangeDate: MutableState<Pair<String, Long>>,
    userId: String,
) {
    val view = LocalView.current

    val lazyListState = rememberLazyListState()

    var indexFrom by remember { mutableIntStateOf(0) }
    var indexTo by remember { mutableIntStateOf(0) }
    var indexStart by remember { mutableIntStateOf(0) }
    val reorderableLazyColumnState =
        rememberReorderableLazyColumnState(lazyListState) { from, to ->

            indexTo = to.index
            indexFrom = from.index

            onEvent(FamilyTasksScreenEvent.MoveItemInList(indexFrom, indexTo))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                view.performHapticFeedback(
                    HapticFeedbackConstants.SEGMENT_FREQUENT_TICK
                )
            }
        }

    LazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(top = 4.dp, bottom = 4.dp),
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        itemsIndexed(
            items = state.value.filterTasks,
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
                    familyMembers = state.value.familyMembers,
                    onEvent = onEvent,
                    userId = userId,
                    selectedTaskForChangeDateUrl = selectedTaskForChangeDate,
                    modifier = Modifier
                        .longPressDraggableHandle(
                            enabled = state.value.filterDateNow,
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
                                    onEvent(
                                        FamilyTasksScreenEvent
                                            .ChangeLocationItemInList(
                                                urlFrom = state.value.filterTasks[indexTo].value.url,
                                                urlTo = state.value.filterTasks[indexFrom].value.url
                                            )
                                    )
                                    indexFrom = 0
                                    indexTo = 0
                                }
                            },
                        )
                        .shadow(elevation = elevation, shape = MaterialTheme.shapes.medium)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DemoLazyColumTasksView() {
    RastilkaTheme {
        LazyColumTasksView(
            state = remember {
                mutableStateOf(FamilyTasksScreenState(filterTasks = SupportPreview.listTask))
            },
            onEvent = {},
            selectedTaskForChangeDate = remember {
                mutableStateOf(Pair("", 0L))
            },
            userId = "",
        )
    }
}
