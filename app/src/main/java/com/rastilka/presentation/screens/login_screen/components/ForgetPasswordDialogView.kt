package com.rastilka.presentation.screens.login_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rastilka.common.app_data.LoadingState
import com.rastilka.presentation.components_app.edit_text_field.EditTextFieldView
import com.rastilka.presentation.components_app.edit_text_field.data.EditTextField
import com.rastilka.presentation.screens.login_screen.data.LoginScreenEvent
import com.rastilka.presentation.screens.login_screen.data.LoginScreenState
import com.rastilka.presentation.ui.theme.RastilkaTheme

@Composable
fun ForgetPasswordDialogView(
    state: LoginScreenState,
    onEvent: (LoginScreenEvent) -> Unit,
) {

    AlertDialog(
        onDismissRequest = {},
        containerColor = MaterialTheme.colorScheme.background,
        confirmButton = {
            TextButton(
                enabled = state.loadingStateForgetPassword != LoadingState.Loading,
                onClick = {
                    onEvent(LoginScreenEvent.ConfirmationForgetPassword)
                }
            ) {
                Text(text = "Подтвердить")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onEvent(
                        LoginScreenEvent.VisibleForgetPasswordDialog(false)
                    )
                }
            ) {
                Text(text = "Отмена")
            }
        },
        title = {
            Text(
                text = "Забыли пароль?",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            val email = EditTextField(
                value = state.emailForgetPassword,
                textLabel = "Введите почту",
                textError = state.errorEmailForgetPassword,
                onValueChange = { text ->
                    onEvent(LoginScreenEvent.EditEmailForgetPassword(text))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = true,
                    imeAction = ImeAction.Next
                )
            )
            Column {

                EditTextFieldView(
                    value = email.value,
                    onValueChange = email.onValueChange,
                    labelText = email.textLabel,
                    textError = email.textError,
                    keyboardOptions = email.keyboardOptions,
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (state.loadingStateForgetPassword == LoadingState.Loading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun DemoForgetPasswordDialogView() {
    RastilkaTheme {
        ForgetPasswordDialogView(
            state = LoginScreenState(),
            onEvent = {},
        )
    }
}
