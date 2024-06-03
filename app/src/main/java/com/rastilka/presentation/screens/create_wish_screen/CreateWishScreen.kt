package com.rastilka.presentation.screens.create_wish_screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.rastilka.R
import com.rastilka.common.app_data.LoadingState
import com.rastilka.presentation.screens.create_wish_screen.data.CreateWishEvent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateWishScreen(
    navController: NavController,
    viewModel: CreateWishViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val focusManager = LocalFocusManager.current

    val configurationDevice = LocalConfiguration.current

    val imeIsVisible = WindowInsets.isImeVisible
    val maxCharPrice = 4
    val maxTextTitle = 2000

    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    val singlePhotoPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                selectedImage = uri
            }
        }

    LaunchedEffect(key1 = imeIsVisible) {
        if (!imeIsVisible) {
            focusManager.clearFocus()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Создать желание", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        focusManager.clearFocus()
                        navController.popBackStack()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back_24),
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(innerPadding)
                .padding(start = 10.dp, end = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = selectedImage,
                    contentDescription = null,
                    error = painterResource(id = R.drawable.ic_image_24),
                    modifier = Modifier
                        .size(configurationDevice.screenHeightDp.dp / 4)
                        .align(Alignment.Center)
                        .padding(bottom = 10.dp)
                        .clickable {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                )
                if (selectedImage != null) {
                    IconButton(
                        onClick = { selectedImage = null },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close_24),
                            contentDescription = null
                        )
                    }
                }
            }
            Text(
                text = "Описание",
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            BasicTextField(
                value = state.titleText,
                onValueChange = { text ->
                    viewModel.onEvent(CreateWishEvent.ChangeTitle(text.take(maxTextTitle)))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = true,
                    imeAction = ImeAction.Default
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 20.sp
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .heightIn(min = 50.dp)
                            .background(Color(0xFFF4F5FA))
                    ) {
                        Box(modifier = Modifier.padding(10.dp)) {
                            innerTextField()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "${state.titleText.chars().count()}/$maxTextTitle",
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    text = "Укажите количество баллов:",
                    fontWeight = FontWeight.Bold,
                )
                BasicTextField(
                    value = state.countPrice,
                    onValueChange = {
                        viewModel.onEvent(
                            CreateWishEvent.ChangeCountPrice(
                                it.take(
                                    maxCharPrice
                                )
                            )
                        )
                    },
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    ),
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            innerTextField()
                            HorizontalDivider(
                                modifier = Modifier
                                    .width(50.dp)
                                    .align(Alignment.BottomCenter)
                            )
                        }
                    },
                )
            }
            Button(
                enabled = state.loadingState == LoadingState.SuccessfulLoad,
                onClick = {
                    focusManager.clearFocus()
                    viewModel.onEvent(
                        CreateWishEvent.CreateWish(
                            navController = navController,
                            uri = selectedImage
                        )
                    )
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                if (state.loadingState != LoadingState.Loading) {
                    Text(text = "Сохранить")
                } 
                else {
                    CircularProgressIndicator()
                }
            }
        }
        if (state.loadingState == LoadingState.FailedLoad) {
            AlertDialog(
                modifier = Modifier.size(100.dp),
                onDismissRequest = {},
                confirmButton = {},
                text = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = state.errorMessage)
                    }
                }
            )
        }
    }
}
