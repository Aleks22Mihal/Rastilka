package com.rastilka.presentation.screens.create_wish_screen.data

import android.net.Uri
import androidx.navigation.NavController

sealed class CreateWishEvent {
    data class ChangeTitle(val text: String) : CreateWishEvent()

    data class ChangeCountPrice(val countPrice: String) : CreateWishEvent()

    data class CreateWish(
        val navController: NavController,
        val uri: Uri?
    ) : CreateWishEvent()
}