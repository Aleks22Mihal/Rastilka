package com.rastilka.presentation.screens.create_wish_screen.data

import com.rastilka.common.app_data.LoadingState
import com.rastilka.domain.models.User

data class CreateWishState(
    val titleText: String = "",
    val countPrice: String = "",
    val user: User? = null,
    val loadingState: LoadingState = LoadingState.SuccessfulLoad,
    val errorMessage: String = ""
)
