package com.rastilka.presentation.screens.family_screen.data

import com.rastilka.common.app_data.LoadingState
import com.rastilka.domain.models.User

data class FamilyScreenState(
    val familyList :List<User> = emptyList(),
    val user: User? = null,
    val initLoadingState: LoadingState = LoadingState.SuccessfulLoad,
    val loadingState: LoadingState = LoadingState.SuccessfulLoad,
    val isOpenBottomSheet: Boolean = false,
    val errorText: String? = null,
    val isOpenErrorDialog: Boolean = false
)
