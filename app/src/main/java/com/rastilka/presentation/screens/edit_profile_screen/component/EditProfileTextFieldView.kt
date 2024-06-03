package com.rastilka.presentation.screens.edit_profile_screen.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rastilka.presentation.components_app.animate_vertical_alignment_as_state.animateHorizontalAlignmentAsState
import com.rastilka.presentation.components_app.clean_focus_keyboard.clearFocusOnKeyboardDismiss
import com.rastilka.presentation.ui.theme.RastilkaTheme

@Composable
fun EditProfileTextFieldView(
    value: String,
    labelText: String? = null,
    textError: String? = null,
    keyboardOptions: KeyboardOptions,
    onValueChange: (String) -> Unit,
) {
    var focusState by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            fontSize = 16.sp
        ),
        decorationBox = { innerTextField ->

            val alignmentAnimate by animateHorizontalAlignmentAsState(
                targetBiasValueVertical = if (focusState || value.isNotBlank()) -1f else 0f
            )

            val fontSizeDpAnimate by animateDpAsState(
                targetValue = if (!focusState && value.isBlank()) 18.dp else 12.dp,
                label = "",
            )

            val colorAnimateLabel by animateColorAsState(
                targetValue = if (!focusState && value.isBlank()) {
                    Color.Black
                } else if (textError != null) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                },
                label = "",
            )

            val colorAnimateBorder by animateColorAsState(
                targetValue = if (textError != null) {
                    MaterialTheme.colorScheme.error
                } else {
                    Color.Transparent
                },
                label = "",
            )

            Column {
                Box(
                    contentAlignment = if (labelText != null) {
                        Alignment.BottomStart
                    } else {
                        Alignment.CenterStart
                    },
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .fillMaxWidth()
                        .heightIn(min = 50.dp)
                        .background(Color(0xFFF4F5FA))
                        .border(
                            width = 1.dp,
                            color = colorAnimateBorder,
                            shape = MaterialTheme.shapes.medium
                        )

                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 15.dp,
                                bottom = if (labelText != null) 5.dp else 0.dp
                            )
                    ) {
                        innerTextField()
                    }

                    if (labelText != null) {
                        Text(
                            text = labelText,
                            fontSize = with(LocalDensity.current) {
                                fontSizeDpAnimate.toSp()
                            },
                            color = colorAnimateLabel,
                            modifier = Modifier
                                .padding(start = 15.dp)
                                .align(alignmentAnimate)
                        )
                    }
                }

                AnimatedVisibility(visible = textError != null) {
                    Text(
                        text = textError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End,
                        lineHeight = 18.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp)
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clearFocusOnKeyboardDismiss()
            .onFocusChanged { focus ->
                focusState = focus.isFocused
            }
    )
}

@Preview
@Composable
private fun DemoEditProfileTextFieldView() {
    RastilkaTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EditProfileTextFieldView(
                value = "",
                labelText = "Ваше Имя",
                onValueChange = { },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = true,
                    imeAction = ImeAction.Next
                )
            )

            EditProfileTextFieldView(
                value = "Имя",
                labelText = "Ваше Имя",
                onValueChange = { },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = true,
                    imeAction = ImeAction.Next
                )
            )

            EditProfileTextFieldView(
                value = "Имя",
                labelText = "Ваше Имя",
                textError = "Длинная ошибка, которую нельзя исправить, просто так",
                onValueChange = { },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = true,
                    imeAction = ImeAction.Next
                )
            )

            EditProfileTextFieldView(
                value = "Имя",
                onValueChange = { },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = true,
                    imeAction = ImeAction.Next
                )
            )
        }
    }
}
