package com.rastilka.presentation.screens.create_task_screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.rastilka.R
import com.rastilka.common.app_data.LoadingState
import com.rastilka.common.utilits_support_preview.SupportPreview
import com.rastilka.presentation.components_app.coil_image_view.ImageLoadCoil
import com.rastilka.presentation.screens.create_task_screen.data.CreateTaskEvent
import com.rastilka.presentation.screens.create_task_screen.data.CreateTaskState
import com.rastilka.presentation.ui.theme.RastilkaTheme
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateTaskScreen(
    navController: NavController,
    onEvent: (CreateTaskEvent) -> Unit,
    state: State<CreateTaskState>,
) {
    val focusManager = LocalFocusManager.current

    val configurationDevice = LocalConfiguration.current

    val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

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
                    Text(text = "Создать задачу")
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = format.format(Date(state.value.dateMillis)),
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
                FloatingActionButton(
                    onClick = {
                        onEvent(CreateTaskEvent.OpenDatePickerDialog(true))
                    },
                    containerColor = MaterialTheme.colorScheme.background
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = null
                    )
                }
            }
            Text(
                text = "Описание",
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            BasicTextField(
                value = state.value.titleText,
                onValueChange = {
                    onEvent(CreateTaskEvent.ChangeTitle(it.take(maxTextTitle)))
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
                            .heightIn(min = 200.dp)
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
                text = "${state.value.titleText.chars().count()}/$maxTextTitle",
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Назначить отвественного",
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            LazyRow(
                contentPadding = PaddingValues(start = 4.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(state.value.listFamilyMembers) { familyMember ->
                    ImageLoadCoil(
                        model = familyMember.picture.toString(),
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.extraLarge)
                            .border(
                                width = 2.dp,
                                shape = MaterialTheme.shapes.extraLarge,
                                color = if (familyMember.id == state.value.selectedFamilyMemberId) {
                                    MaterialTheme.colorScheme.primary
                                } else Color.Transparent
                            )
                            .size(50.dp)
                            .clickable {
                                onEvent(
                                    CreateTaskEvent.SelectUserId(familyMember.id)
                                )
                            },
                        contentDescription = "Avatar Person",
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp, top = 20.dp)
            ) {
                Text(
                    text = "Укажите количество баллов:",
                    fontWeight = FontWeight.Bold,
                )
                BasicTextField(
                    value = state.value.countPrice,
                    onValueChange = {
                        onEvent(CreateTaskEvent.ChangeCountPrice(it.take(maxCharPrice)))
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
                enabled = state.value.loadingState == LoadingState.SuccessfulLoad,
                onClick = {
                    focusManager.clearFocus()
                    onEvent(
                        CreateTaskEvent.CreateTask(
                            navController = navController,
                            uri = selectedImage
                        )
                    )
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Создать")
            }
        }
        if (state.value.isOpenDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = state.value.dateMillis,
                yearRange = LocalDate.now().year..DatePickerDefaults.YearRange.last,
                selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {

                        val dateNow = LocalDateTime.now()
                            .minusDays(1)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()

                        return utcTimeMillis >= dateNow
                    }

                    override fun isSelectableYear(year: Int): Boolean {
                        return year >= LocalDate.now().year
                    }
                }
            )

            DatePickerDialog(
                onDismissRequest = {
                    onEvent(CreateTaskEvent.OpenDatePickerDialog(false))
                },
                confirmButton = {
                    TextButton(onClick = {
                        onEvent(
                            CreateTaskEvent.SetSelectedDate(
                                datePickerState.selectedDateMillis ?: 0L
                            )
                        )
                        onEvent(CreateTaskEvent.OpenDatePickerDialog(false))
                    }) {
                        Text(text = "Изменить")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
        if (state.value.loadingState == LoadingState.Loading) {
            AlertDialog(
                modifier = Modifier.size(100.dp),
                onDismissRequest = {},
                confirmButton = {},
                text = {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            )
        }
        if (state.value.loadingState == LoadingState.FailedLoad) {
            AlertDialog(
                modifier = Modifier.size(100.dp),
                onDismissRequest = {},
                confirmButton = {},
                text = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = state.value.errorMessage)
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun DemoCreateTask() {
    RastilkaTheme {
        CreateTaskScreen(
            navController = rememberNavController(),
            onEvent = {},
            state = remember {
                mutableStateOf(
                    CreateTaskState(
                        user = SupportPreview.user,
                        listFamilyMembers = SupportPreview.listFamily
                    )
                )
            }
        )
    }
}