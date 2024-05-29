package com.rastilka.presentation.screens.create_task_screen.data

import com.rastilka.common.app_data.LoadingState
import com.rastilka.domain.models.User

data class CreateTaskState(
    val titleText: String = "",
    val countPrice: String = "",
    val isOpenDatePicker: Boolean = false,
    val dateMillis: Long = 0L,
    val user: User? = null,
    val listFamilyMembers : List<User> = emptyList(),
    val selectedFamilyMemberId: String = "",
    val loadingState: LoadingState = LoadingState.SuccessfulLoad,
    val errorMessage: String = ""
)
