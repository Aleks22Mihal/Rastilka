package com.rastilka.presentation.screens.login_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import com.rastilka.R
import com.rastilka.presentation.components_app.edit_text_field.EditTextFieldView
import com.rastilka.presentation.components_app.edit_text_field.data.EditTextField
import com.rastilka.presentation.screens.login_screen.data.LoginScreenEvent
import com.rastilka.presentation.screens.login_screen.data.LoginScreenState

@Composable
fun RegistrationView(
    state: LoginScreenState,
    onEvent: (LoginScreenEvent) -> Unit,
    loginOnClick: () -> Unit,
) {

    val nameUser = EditTextField(
        value = state.nameRegistration,
        textLabel = "Введите имя",
        textError = state.errorNameRegistration,
        onValueChange = { text ->
            onEvent(LoginScreenEvent.EditNameRegistration(text))
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrect = true,
            imeAction = ImeAction.Next
        )
    )

    val emailReg = EditTextField(
        value = state.emailRegistration,
        textLabel = "Введите почту",
        textError = state.errorEmailRegistration,
        onValueChange = { text ->
            onEvent(LoginScreenEvent.EditEmailRegistration(text))
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrect = true,
            imeAction = ImeAction.Next
        )
    )

    val passwordReg = EditTextField(
        value = state.passwordRegistration,
        textLabel = "Придумайте пароль",
        textError = state.errorPasswordRegistration,
        onValueChange = { text ->
            onEvent(LoginScreenEvent.EditPasswordRegistration(text))
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrect = true,
            imeAction = ImeAction.Next
        ),
        visualTransformation = if (state.isVisiblePasswordRegistration) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(onClick = {
                onEvent(
                    LoginScreenEvent.VisiblePasswordRegistration(
                        !state.isVisiblePasswordRegistration
                    )
                )
            }) {
                Icon(
                    painter = painterResource(
                        id = if (state.isVisiblePasswordRegistration) {
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
    val listRegistrationTextFiled =
        listOf(nameUser, emailReg, passwordReg)

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
                text = "Создайте Ваш аккаунт",
                fontWeight = FontWeight.Light,
            )

            listRegistrationTextFiled.forEach { textField ->
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
        }
        Button(
            onClick = {
                onEvent(LoginScreenEvent.Registration)
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
                text = "Создать",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
