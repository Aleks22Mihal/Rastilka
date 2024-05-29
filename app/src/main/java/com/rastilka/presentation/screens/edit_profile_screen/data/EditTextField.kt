package com.rastilka.presentation.screens.edit_profile_screen.data

import androidx.compose.foundation.text.KeyboardOptions

data class EditTextField(
    val value: String,
    val textLabel: String,
    val textError: String?,
    val onValueChange: (String) -> Unit,
    val keyboardOptions: KeyboardOptions,
)
