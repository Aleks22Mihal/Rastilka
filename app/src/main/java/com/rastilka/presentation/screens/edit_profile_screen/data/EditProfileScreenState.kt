package com.rastilka.presentation.screens.edit_profile_screen.data

import com.rastilka.common.app_data.LoadingState
import com.rastilka.domain.models.User

data class EditProfileScreenState(
    val user: User? = null,
    val editPhoto: String? = null,
    val editName: String = "",
    val editNameError: String? = null,
    val editEmail: String = "",
    val editEmailError: String? = null,
    val editPassword: String? = null,
    val editPasswordError: String? = null,
    val initLoadingState: LoadingState = LoadingState.SuccessfulLoad,
    val loadingState: LoadingState = LoadingState.SuccessfulLoad,
    val screenErrorMessage: String = ""
)
