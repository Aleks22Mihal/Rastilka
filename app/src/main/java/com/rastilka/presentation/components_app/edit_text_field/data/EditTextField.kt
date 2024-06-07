package com.rastilka.presentation.components_app.edit_text_field.data

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.VisualTransformation

data class EditTextField(
    val value: String,
    val textLabel: String,
    val textError: String?,
    val onValueChange: (String) -> Unit,
    val keyboardOptions: KeyboardOptions,
    val trailingIcon: @Composable (() -> Unit)? = null,
    val visualTransformation: VisualTransformation = VisualTransformation.None
)
