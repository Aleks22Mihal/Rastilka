package com.rastilka.presentation.screens.profile_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rastilka.common.utilits_support_preview.SupportPreview
import com.rastilka.domain.models.User
import com.rastilka.presentation.components_app.coil_image_view.ImageLoadCoil
import com.rastilka.presentation.ui.theme.RastilkaTheme

@Composable
fun UserView(user: User?) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier = Modifier.clip(MaterialTheme.shapes.extraLarge)
        ) {
            ImageLoadCoil(
                model = user?.picture.toString(),
                contentScale = ContentScale.Crop,
                contentDescription = "Profile Avatar",
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
            )
        }
        Text(text = "Почта")
        Text(text = "${user?.mail}")
    }
}

@Preview
@Composable
private fun UserViewDemo() {
    RastilkaTheme {
        UserView(user = SupportPreview.user)
    }
}