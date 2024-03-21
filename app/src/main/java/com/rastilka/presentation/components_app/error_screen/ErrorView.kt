package com.rastilka.presentation.components_app.error_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rastilka.R

@Composable
fun ErrorView(
    refreshFun: () -> Unit,
    textError: String = "Что-то пошло не так!"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.image_error),
            contentDescription = "Error Icon",
            tint = Color.Unspecified
        )
        Text(
            text = textError,
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = { refreshFun() }) {
            Text(
                text = "Попробуйте еще раз",
            )
        }
    }
}