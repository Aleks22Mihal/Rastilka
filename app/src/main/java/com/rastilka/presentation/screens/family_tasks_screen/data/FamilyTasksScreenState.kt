package com.rastilka.presentation.screens.family_tasks_screen.data

import com.rastilka.common.app_data.LoadingState
import com.rastilka.domain.models.TaskOrWish
import com.rastilka.domain.models.User

data class FamilyTasksScreenState(
    val familyMembers: List<User> = emptyList(),
    val tasksList: List<TaskOrWish> = emptyList(),
    val user: User? = null,
    val mainFolderTask: TaskOrWish? = null,
    val filterTasks: List<TaskOrWish> = emptyList(),
    val initLoadingState: LoadingState = LoadingState.Loading,
    val loadingState: LoadingState = LoadingState.SuccessfulLoad,
    val errorMessage: String = ""
)
