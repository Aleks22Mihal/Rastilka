package com.rastilka.common.app_data

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.MutableState

data class TextInputState(
    val nameTextField: String?,
    val text: MutableState<String>,
    val isError: MutableState<Boolean>,
    val keyboardOptions: KeyboardOptions,
    val keyboardActions: KeyboardActions? = null,
    val textError: MutableState<String>
)
