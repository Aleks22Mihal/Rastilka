package com.rastilka.presentation.screens.family_wishes_screen.data

import com.rastilka.common.app_data.LoadingState
import com.rastilka.domain.models.TaskOrWish
import com.rastilka.domain.models.User

data class FamilyWishScreenState(
    val familyMembers: List<User> = emptyList(),
    val wishList: List<TaskOrWish> = emptyList(),
    val initLoadingState: LoadingState = LoadingState.Loading,
    val loadingState: LoadingState = LoadingState.SuccessfulLoad,
    val errorMessage: String = ""
)