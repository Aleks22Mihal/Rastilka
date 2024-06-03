package com.rastilka.presentation.screens.technical_support_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rastilka.R
import com.rastilka.common.app_data.LoadingState
import com.rastilka.presentation.screens.technical_support_screen.data.TechnicalSupportEvent
import com.rastilka.presentation.screens.technical_support_screen.data.TechnicalSupportState
import com.rastilka.presentation.ui.theme.RastilkaTheme

@Composable
fun TechnicalChatBottomSendView(
    state: State<TechnicalSupportState>,
    onEvent: (TechnicalSupportEvent) -> Unit,
    modifier: Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(
                8.dp,
                Alignment.CenterHorizontally
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            BasicTextField(
                value = state.value.message,
                onValueChange = {
                    onEvent(TechnicalSupportEvent.ChangeMessage(it))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = true,
                    imeAction = ImeAction.Default
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 18.sp
                ),
                maxLines = 4,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .heightIn(min = 20.dp)
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = MaterialTheme.shapes.medium
                            )
                            .background(Color(0xFFFFFFFF))
                    ) {
                        Box(modifier = Modifier.padding(5.dp)) {
                            innerTextField()
                        }
                    }
                },
                modifier = Modifier.weight(0.8f)
            )
            IconButton(
                enabled = state.value.loadingState == LoadingState.SuccessfulLoad,
                onClick = {
                    onEvent(TechnicalSupportEvent.SendMessage)
                },
                modifier = Modifier.size(35.dp)
            ) {
                if (state.value.loadingState == LoadingState.SuccessfulLoad) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_send),
                        contentDescription = null,
                    )
                } else {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DemoTechnicalChatBottomSendView() {
    RastilkaTheme {
        TechnicalChatBottomSendView(
            state = remember {
                mutableStateOf(TechnicalSupportState())
            },
            onEvent = {},
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface
                )
        )
    }
}
