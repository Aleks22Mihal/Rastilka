package com.rastilka.presentation.screens.login_screen.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.rastilka.common.app_data.LoggingStatus
import com.rastilka.common.app_data.TextInputState
import com.rastilka.presentation.screens.login_screen.LoginViewModel

@Composable
fun LoginView(
    viewModel: LoginViewModel,
    userStatus: LoggingStatus?,
) {

    val emailUser = TextInputState(
        nameTextField = "Email",
        text = remember { mutableStateOf("") },
        isError = remember { mutableStateOf(false) },
        keyboardOptions = KeyboardOptions.Default.copy(
            autoCorrect = true,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        textError = remember { mutableStateOf("") },
    )

    val passwordUser = TextInputState(
        nameTextField = "Пароль",
        text = remember { mutableStateOf("") },
        isError = remember { mutableStateOf(false) },
        keyboardOptions = KeyboardOptions.Default.copy(
            autoCorrect = true,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        textError = remember { mutableStateOf("") },
    )

    val listText = listOf(emailUser, passwordUser)

    listText.forEach { textInput ->
        TextFieldView(inputText = textInput)
    }
    TextButton(onClick = { /*TODO*/ }) {
        Text(text = "Забыли пароль?")
    }
    Button(
        onClick = {
            listText.forEach { textUser ->
                if (textUser.text.value.isEmpty()) {
                    textUser.isError.value = true
                    textUser.textError.value = "Пустое поле"
                }
            }
            if (!passwordUser.isError.value && !emailUser.isError.value) {
                viewModel.logIn(email = emailUser.text.value, password = passwordUser.text.value)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = "Войти", fontSize = 20.sp)
    }

    LaunchedEffect(key1 = userStatus) {
        when (userStatus) {
            LoggingStatus.mailIsntExist -> {
                emailUser.isError.value = true
                emailUser.textError.value = "Такой пользователь не зарегистрирован"
            }

            LoggingStatus.passwordError -> {
                passwordUser.isError.value = true
                passwordUser.textError.value = "Пароль не верный"
            }

            LoggingStatus.getParametersError -> {
                emailUser.isError.value = true
                passwordUser.isError.value = true
                passwordUser.textError.value = "Неверный параметры"
                emailUser.textError.value = "Неверный параметры"
            }

            LoggingStatus.mailIsBusy -> {
                emailUser.isError.value = true
                emailUser.textError.value = "Почта занята"
            }

            LoggingStatus.mailForbidden -> {
                emailUser.isError.value = true
                emailUser.textError.value = "Не верный формат почты"
            }

            LoggingStatus.mailWasFromOldSite -> {
                emailUser.isError.value = true
                emailUser.textError.value = "Почта была на старом сайте"
            }

            else -> {}
        }
    }
}