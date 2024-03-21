package com.rastilka.presentation.screens.family_screen.views

import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rastilka.presentation.screens.family_screen.FamilyViewModel

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomQrCodeSheet(
    textId: String?,
    showBottomSheet: MutableState<Boolean>,
    sheetState: SheetState,
    viewModel: FamilyViewModel
) {
    val scope = rememberCoroutineScope()

    var openCameraQrCode by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (openCameraQrCode) {
            CameraView(
                showBottomSheet = showBottomSheet,
                sheetState = sheetState,
                scope = scope,
                userId = textId,
                viewModel = viewModel
            )
        } else {
            QrCodeView(textId = textId)
        }
        Button(
            onClick = { openCameraQrCode = !openCameraQrCode },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
        ) {
            Text(text = if (openCameraQrCode) "Назад" else "Камера")
        }
    }
}