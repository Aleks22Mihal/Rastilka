package com.rastilka.presentation.screens.login_screen.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.rastilka.R
import com.rastilka.common.app_data.TextInputState
import com.rastilka.presentation.components_app.clean_focus_keyboard.clearFocusOnKeyboardDismiss

@Composable
fun TextFieldView(
    inputText: TextInputState,
) {
    var showPassword by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = inputText.text.value,
        onValueChange = { changeText ->
            if (inputText.isError.value) {
                inputText.isError.value = false
                inputText.textError.value = ""
            }
            inputText.text.value = changeText
        },
        label = {
            if (inputText.nameTextField != null) {
                Text(text = inputText.nameTextField)
            }
        },
        supportingText = {
            Text(
                text = inputText.textError.value,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        },
        isError = inputText.isError.value,
        singleLine = true,
        visualTransformation = if (KeyboardType.Password == inputText.keyboardOptions.keyboardType) {
            if (showPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            }
        } else VisualTransformation.None,
        keyboardOptions = inputText.keyboardOptions,
        keyboardActions = inputText.keyboardActions ?: KeyboardActions.Default,
        trailingIcon = {
            if (KeyboardType.Password == inputText.keyboardOptions.keyboardType) {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        painter = painterResource(
                            id = if (showPassword) {
                                R.drawable.baseline_visibility_24
                            } else R.drawable.baseline_visibility_off_24
                        ),
                        contentDescription = "Visibility",
                    )
                }
            }
        },
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .fillMaxWidth()
            .clearFocusOnKeyboardDismiss()
    )
}