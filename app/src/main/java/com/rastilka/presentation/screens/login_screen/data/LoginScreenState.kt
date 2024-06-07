package com.rastilka.presentation.screens.login_screen.data

import com.rastilka.common.app_data.LoadingState
import com.rastilka.common.app_data.LoggingStatus
import com.rastilka.domain.models.User

data class LoginScreenState(
    val user: User? = null,
    val userStatus: LoggingStatus? = null,
    val loadingState: LoadingState = LoadingState.SuccessfulLoad,
    val initLoadingState: LoadingState = LoadingState.SuccessfulLoad,
    val email: String = "",
    val errorEmail: String? = null,
    val password: String = "",
    val errorPassword: String? = null,
    val nameRegistration: String = "",
    val errorNameRegistration: String? = null,
    val emailRegistration: String = "",
    val errorEmailRegistration: String? = null,
    val passwordRegistration: String = "",
    val errorPasswordRegistration: String? = null,
    val isVisiblePassword: Boolean = false,
    val isVisiblePasswordRegistration: Boolean = false,
    val emailForgetPassword: String = "",
    val errorEmailForgetPassword: String? = null,
    val isVisibleForgetPasswordDialog: Boolean = false,
    val loadingStateForgetPassword: LoadingState = LoadingState.SuccessfulLoad,
    val isSnackBarShowing: Boolean = false
)
