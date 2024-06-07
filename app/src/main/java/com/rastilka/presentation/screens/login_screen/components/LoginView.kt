package com.rastilka.presentation.screens.login_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rastilka.R
import com.rastilka.presentation.components_app.edit_text_field.EditTextFieldView
import com.rastilka.presentation.components_app.edit_text_field.data.EditTextField
import com.rastilka.presentation.screens.login_screen.data.LoginScreenEvent
import com.rastilka.presentation.screens.login_screen.data.LoginScreenState
import com.rastilka.presentation.ui.theme.RastilkaTheme

@Composable
fun LoginView(
    state: LoginScreenState,
    onEvent: (LoginScreenEvent) -> Unit,
    registrationOnClick: () -> Unit,
) {

    val email = EditTextField(
        value = state.email,
        textLabel = "Введите почту",
        textError = state.errorEmail,
        onValueChange = { text ->
            onEvent(LoginScreenEvent.EditEmail(text))
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrect = true,
            imeAction = ImeAction.Next
        )
    )

    val password = EditTextField(
        value = state.password,
        textLabel = "Введите пароль",
        textError = state.errorPassword,
        onValueChange = { text ->
            onEvent(LoginScreenEvent.EditPassword(text))
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrect = true,
            imeAction = ImeAction.Done
        ),
        visualTransformation = if (state.isVisiblePassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(onClick = {
                onEvent(LoginScreenEvent.VisiblePassword(!state.isVisiblePassword))
            }) {
                Icon(
                    painter = painterResource(
                        id = if (state.isVisiblePassword) {
                            R.drawable.baseline_visibility_off_24
                        } else {
                            R.drawable.baseline_visibility_24
                        }
                    ),
                    contentDescription = "Visibility",
                )
            }
        }
    )

    val listTextField = listOf(email, password)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.medium
                )
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            Text(
                text = "Войдите в Ваш аккаунт",
                fontWeight = FontWeight.Light,
            )

            listTextField.forEach { textField ->
                EditTextFieldView(
                    value = textField.value,
                    keyboardOptions = textField.keyboardOptions,
                    onValueChange = textField.onValueChange,
                    trailingIcon = textField.trailingIcon,
                    visualTransformation = textField.visualTransformation,
                    textError = textField.textError,
                    labelText = textField.textLabel
                )
            }
            TextButton(
                onClick = {
                    onEvent(
                        LoginScreenEvent
                            .VisibleForgetPasswordDialog(
                                !state.isVisibleForgetPasswordDialog
                            )
                    )
                },
                modifier = Modifier.height(30.dp)
            ) {
                Text(
                    text = "Забыли пароль?",
                    fontSize = 14.sp,
                    lineHeight = 16.sp
                )
            }
        }
        Button(
            onClick = {
                onEvent(LoginScreenEvent.LogIn)
            },
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            contentPadding = PaddingValues(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Войти",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun DemoLoginTextField() {
    RastilkaTheme {
        LoginView(
            state = LoginScreenState(),
            onEvent = {},
            registrationOnClick = {}
        )
    }
}
