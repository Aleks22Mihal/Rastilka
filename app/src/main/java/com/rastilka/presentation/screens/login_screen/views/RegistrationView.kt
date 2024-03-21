package com.rastilka.presentation.screens.login_screen.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.rastilka.common.app_data.TextInputState
import com.rastilka.presentation.screens.login_screen.LoginViewModel

@Composable
fun RegistrationView(viewModel: LoginViewModel) {

    val nameUser = TextInputState(
        nameTextField = "Ваше имя",
        text = remember { mutableStateOf("") },
        isError = remember { mutableStateOf(false) },
        keyboardOptions = KeyboardOptions.Default.copy(
            autoCorrect = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        textError = remember { mutableStateOf("") },
    )
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
    val phoneUser = TextInputState(
        nameTextField = "Номер телефона",
        text = remember { mutableStateOf("") },
        isError = remember { mutableStateOf(false) },
        keyboardOptions = KeyboardOptions.Default.copy(
            autoCorrect = true,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        textError = remember { mutableStateOf("") },
    )
    val passwordUser = TextInputState(
        nameTextField = "Придумайте пароль",
        text = remember { mutableStateOf("") },
        isError = remember { mutableStateOf(false) },
        keyboardOptions = KeyboardOptions.Default.copy(
            autoCorrect = true,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        textError = remember { mutableStateOf("") },
    )
    val passwordRepUser = TextInputState(
        nameTextField = "Повторите пароль",
        text = remember { mutableStateOf("") },
        isError = remember { mutableStateOf(false) },
        keyboardOptions = KeyboardOptions.Default.copy(
            autoCorrect = true,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        textError = remember { mutableStateOf("") },
    )

    val listRegistrationTextFiled =
        listOf(nameUser, emailUser, phoneUser, passwordUser, passwordRepUser)

    listRegistrationTextFiled.forEach { textInput ->
        TextFieldView(inputText = textInput)
    }

    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = "Зарегистрироваться", fontSize = 20.sp)
    }
}