package com.rastilka.presentation.screens.technical_support_screen.data

import com.rastilka.common.app_data.LoadingState
import com.rastilka.domain.models.TechnicalSupportMessage
import com.rastilka.domain.models.User

data class TechnicalSupportState(
    val listMessage: List<TechnicalSupportMessage> = emptyList(),
    val user: User? = null,
    val message: String = "",
    val errorMessage: String = "",
    val initLoadingState: LoadingState = LoadingState.SuccessfulLoad,
    val loadingState: LoadingState = LoadingState.SuccessfulLoad,
)
