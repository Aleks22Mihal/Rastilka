package com.rastilka.presentation.screens.family_screen.componets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rastilka.R
import com.rastilka.presentation.components_app.generate_qr_code.rememberQrBitmapPainter

@Composable
fun QrCodeView(
    textId: String?
) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (textId != null) {
            val paint = rememberQrBitmapPainter(
                content = textId,
                size = 300.dp,
                padding = 0.dp
            )
            Image(
                painter = paint,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(300.dp)
                    .background(MaterialTheme.colorScheme.background)
            )
            Text(
                text = textId,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.emoji_error),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(300.dp)
                    .background(MaterialTheme.colorScheme.background)
            )
            Text(
                text = "Что-то пошло не так.",
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )
        }
    }
}

@Preview
@Composable
private fun DemoQrCodeView() {
    QrCodeView(textId = null)
}