package com.rastilka.presentation.screens.edit_profile_screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rastilka.common.utilits_support_preview.SupportPreview
import com.rastilka.presentation.screens.edit_profile_screen.data.EditProfileScreenEvent
import com.rastilka.presentation.screens.edit_profile_screen.data.EditProfileScreenState
import com.rastilka.presentation.screens.edit_profile_screen.data.EditTextField
import com.rastilka.presentation.ui.theme.RastilkaTheme


@Composable
fun EditProfileAttributesView(
    state: State<EditProfileScreenState>,
    onEvent: (EditProfileScreenEvent) -> Unit
) {

    val listTextField = listOf(
        EditTextField(
            value = state.value.editName,
            textLabel = "Ваше Имя",
            textError = state.value.editNameError,
            onValueChange = { text ->
                onEvent(EditProfileScreenEvent.EditName(text))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrect = true,
                imeAction = ImeAction.Next
            )
        ),
        EditTextField(
            value = state.value.editEmail,
            textLabel = "Ваша почта",
            textError = state.value.editEmailError,
            onValueChange = { text ->
                onEvent(EditProfileScreenEvent.EditEmail(text))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrect = true,
                imeAction = ImeAction.Next
            )
        ),
        EditTextField(
            value = state.value.editPassword ?: "",
            textLabel = "Новый пароль",
            textError = state.value.editPasswordError,
            onValueChange = { text ->
                onEvent(EditProfileScreenEvent.EditPassword(text))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrect = true,
                imeAction = ImeAction.Default
            )
        ),
        )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.medium
            )
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        listTextField.forEach { editTextField ->

            EditProfileTextFieldView(
                value = editTextField.value,
                textError = editTextField.textError,
                labelText = editTextField.textLabel,
                onValueChange = editTextField.onValueChange,
                keyboardOptions = editTextField.keyboardOptions
            )
        }
    }
}


@Preview
@Composable
private fun DemoEditProfileAttributesView() {
    RastilkaTheme {
        EditProfileAttributesView(
            state = remember {
                mutableStateOf(
                    EditProfileScreenState(
                        user = SupportPreview.user,
                        editPhoto = "1"
                    )
                )
            },
            onEvent = {}
        )
    }
}