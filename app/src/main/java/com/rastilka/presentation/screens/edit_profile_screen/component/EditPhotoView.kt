package com.rastilka.presentation.screens.edit_profile_screen.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rastilka.R
import com.rastilka.common.utilits_support_preview.SupportPreview
import com.rastilka.presentation.components_app.coil_image_view.ImageLoadCoil
import com.rastilka.presentation.screens.edit_profile_screen.data.EditProfileScreenEvent
import com.rastilka.presentation.screens.edit_profile_screen.data.EditProfileScreenState
import com.rastilka.presentation.ui.theme.RastilkaTheme

@Composable
fun EditPhotoView(
    state: State<EditProfileScreenState>,
    onEvent: (EditProfileScreenEvent) -> Unit
) {

    val singlePhotoPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                onEvent(EditProfileScreenEvent.EditPhoto(uri.toString()))
            }
        }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            )
            .fillMaxWidth()
    ) {
        if (state.value.editPhoto.isNullOrBlank()) {
            ImageLoadCoil(
                model = state.value.user?.picture ?: "",
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )
        } else {
            Box {
                AsyncImage(
                    model = state.value.editPhoto,
                    contentDescription = null,
                    error = painterResource(id = R.drawable.ic_image_24),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(color = Color.LightGray)
                        .size(150.dp)

                )
                IconButton(
                    onClick = {
                        onEvent(EditProfileScreenEvent.EditPhoto(null))
                    },
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close_24),
                        contentDescription = null
                    )
                }
            }
        }
        TextButton(onClick = {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }) {
            Text(text = "Изменить фото")
        }
    }
}

@Preview
@Composable
private fun DemoEditPhotoView() {
    RastilkaTheme {
        EditPhotoView(
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