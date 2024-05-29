package com.rastilka.presentation.screens.family_tasks_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rastilka.R
import com.rastilka.common.utilits_support_preview.SupportPreview
import com.rastilka.domain.models.TaskOrWish
import com.rastilka.domain.models.TaskOrWishValue
import com.rastilka.domain.models.User
import com.rastilka.presentation.components_app.clean_focus_keyboard.clearFocusOnKeyboardDismiss
import com.rastilka.presentation.components_app.coil_image_view.ImageLoadCoil
import com.rastilka.presentation.screens.family_tasks_screen.data.FamilyTasksScreenEvent
import com.rastilka.presentation.ui.theme.RastilkaTheme
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardTaskView(
    task: TaskOrWish,
    familyMembers: List<User>,
    userId: String,
    isVisibleDescription: MutableState<Boolean> = remember { mutableStateOf(false) },
    modifier: Modifier,
    onEvent: (FamilyTasksScreenEvent) -> Unit,
    selectedTaskForChangeDateUrl: MutableState<Pair<String, Long>>,
) {
    
    var title by remember {
        mutableStateOf(task.value.h1)
    }

    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = { density ->
            val densityHalf = density / 2f
            densityHalf
        },
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onEvent(FamilyTasksScreenEvent.DeleteTask(productUrl = task.value.url))
                true
            } else false
        }
    )

    val apiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val resultDate = LocalDateTime.parse(task.value.date, apiFormat)
    val format = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())

    val date = if (resultDate > LocalDateTime.now()) {
        resultDate.format(format)
    } else LocalDateTime.now().format(format)

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
                    SwipeToDismissBoxValue.StartToEnd -> Color.Transparent
                    SwipeToDismissBoxValue.Settled -> Color.LightGray
                },
                label = "animateColor"
            )
            val scale by animateFloatAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> 1.5f
                    SwipeToDismissBoxValue.StartToEnd -> 0f
                    SwipeToDismissBoxValue.Settled -> 1f
                },
                label = "animateFloat"
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.large)
                    .background(color = color),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete_24),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .scale(scale)
                )
            }
        },
    ) {
        Card(
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    isVisibleDescription.value = !isVisibleDescription.value
                    if (!isVisibleDescription.value) {
                        title = task.value.h1
                    }
                },
        ) {
            Column(
                modifier = Modifier.padding(5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DidResponsibleUserTaskView(
                        task = task,
                        familyMembers = familyMembers,
                        onEvent = onEvent,
                        userId = userId,
                    )
                    AnimatedVisibility(visible = !isVisibleDescription.value) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(start = 5.dp)
                        ) {
                            Text(
                                text = task.value.h1,
                                fontSize = 16.sp,
                                lineHeight = 21.sp,
                                fontWeight = FontWeight(400),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = date,
                                fontSize = 9.sp,
                                lineHeight = 12.sp,
                                maxLines = 1,
                                fontWeight = FontWeight.Light,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    if (isVisibleDescription.value) {
                        Text(
                            text = "Готово",
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .clickable {
                                    if (task.value.h1 != title) {
                                        onEvent(
                                            FamilyTasksScreenEvent.EditTask(
                                                productUrl = task.value.url,
                                                property = "h1",
                                                value = title
                                            )
                                        )
                                    } else {
                                        isVisibleDescription.value = !isVisibleDescription.value
                                        if (!isVisibleDescription.value) {
                                            title = task.value.h1
                                        }
                                    }
                                }
                        )
                    }
                }

                AnimatedVisibility(visible = isVisibleDescription.value) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        BasicTextField(
                            value = title,
                            onValueChange = {
                                title = it
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            decorationBox = { innerTextField ->
                                Box(
                                    modifier = Modifier
                                        .clip(MaterialTheme.shapes.medium)
                                        .heightIn(min = 40.dp)
                                        .padding(start = 4.dp, end = 4.dp)
                                        .background(Color(0xFFF4F5FA))
                                ) {
                                    Box(modifier = Modifier.padding(10.dp)) {
                                        Column {
                                            innerTextField()
                                            if (task.value.photo != null) {
                                                Box(
                                                    contentAlignment = Alignment.Center,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(top = 4.dp),
                                                ) {
                                                    ImageLoadCoil(
                                                        model = task.value.photo,
                                                        modifier = Modifier.size(150.dp),
                                                        contentDescription = ""
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        )

                        if (!task.value.date.isNullOrBlank()) {
                            DateTaskView(
                                task = task.value,
                                date = date,
                                resultDate = resultDate,
                                selectedTaskForChangeDateUrl = selectedTaskForChangeDateUrl,
                                onEvent = onEvent,
                            )
                        }

                        ResponsibleUserTaskView(
                            task = task,
                            familyMembers = familyMembers,
                            onEvent = onEvent,
                        )

                        PriceTaskView(
                            task = task,
                            tittle = title,
                            onEvent = onEvent,
                        )


                    }
                }
            }
        }
    }
}


@Composable
private fun DidResponsibleUserTaskView(
    task: TaskOrWish,
    familyMembers: List<User>,
    userId: String,
    onEvent: (FamilyTasksScreenEvent) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(end = 4.dp)
    ) {
        familyMembers.forEach { familyMember ->
            if (familyMember.id in task.uuid.forUsers) {
                if (familyMember.id == userId) {
                    RadioButton(
                        selected = familyMember.id in task.uuid.didUsers, onClick = {
                            onEvent(
                                FamilyTasksScreenEvent.SetDidResponsibleUser(
                                    task.value.url,
                                    familyMember.id
                                )
                            )
                        }, modifier = Modifier
                            .clip(CircleShape)
                            .size(40.dp)
                    )
                } else {
                    ImageLoadCoil(
                        model = familyMember.picture.toString(),
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                shape = MaterialTheme.shapes.extraLarge,
                                color = if (familyMember.id in task.uuid.didUsers) {
                                    MaterialTheme.colorScheme.primary
                                } else Color.Transparent
                            )
                            .size(40.dp)
                            .clickable {
                                onEvent(
                                    FamilyTasksScreenEvent.SetDidResponsibleUser(
                                        task.value.url,
                                        familyMember.id
                                    )
                                )
                            },
                        contentDescription = "Avatar Person",
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
private fun ResponsibleUserTaskView(
    task: TaskOrWish,
    familyMembers: List<User>,
    onEvent: (FamilyTasksScreenEvent) -> Unit,
) {
    Column {

        Text(
            text = "Назначить отвественного",
            fontSize = 12.sp,
            lineHeight = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        LazyRow(
            contentPadding = PaddingValues(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(familyMembers) { familyMember ->
                ImageLoadCoil(
                    model = familyMember.picture.toString(),
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraLarge)
                        .border(
                            width = 2.dp,
                            shape = MaterialTheme.shapes.extraLarge,
                            color = if (familyMember.id in task.uuid.forUsers) {
                                MaterialTheme.colorScheme.primary
                            } else Color.Transparent
                        )
                        .size(35.dp)
                        .clickable {
                            onEvent(
                                FamilyTasksScreenEvent.AddResponsibleUser(
                                    productUrl = task.value.url,
                                    userId = familyMember.id
                                )
                            )
                        },
                    contentDescription = "Avatar Person",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
private fun PriceTaskView(
    task: TaskOrWish,
    tittle: String,
    onEvent: (FamilyTasksScreenEvent) -> Unit,
) {
    var priceText by remember { mutableStateOf(task.value.price ?: "") }
    val focus = remember { mutableStateOf(false) }
    val pattern = remember { Regex("^\\d+\$") }
    val maxChar = 4

    Spacer(modifier = Modifier.height(8.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(enabled = priceText.isNotEmpty(),
            onClick = {
                if (priceText.toFloat() != 0f) {
                    onEvent(
                        FamilyTasksScreenEvent.GetPoint(
                            points = priceText,
                            usersId = task.uuid.forUsers,
                            title = tittle,
                            productUrl = task.value.url
                        )
                    )
                }
            }) {
            Image(
                painter = painterResource(id = R.drawable.ic_minus),
                contentDescription = null,
            )
        }
        BasicTextField(
            value = priceText,
            onValueChange = { newText ->
                if (newText.isEmpty() || newText.matches(pattern)) {
                    priceText = newText.take(maxChar)
                }
            },
            textStyle = LocalTextStyle.current.copy(
                lineHeight = 18.sp,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            ),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            decorationBox = { innerTextField ->
                if (priceText.isEmpty() && !focus.value) {
                    Text(
                        text = "0",
                        lineHeight = 18.sp,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
                innerTextField()
            },
            modifier = Modifier
                .width(40.dp)
                .height(18.dp)
                .onFocusChanged {
                    focus.value = it.isFocused
                }
                .clearFocusOnKeyboardDismiss()
        )

        IconButton(
            enabled = priceText.isNotEmpty(),
            onClick = {
                if (priceText.toFloat() != 0f) {
                    onEvent(
                        FamilyTasksScreenEvent.SendPoint(
                            points = priceText,
                            usersId = task.uuid.forUsers,
                            title = tittle,
                            productUrl = task.value.url
                        )
                    )
                }
            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = "Send point"
            )
        }
    }
}

@Composable
fun DateTaskView(
    task: TaskOrWishValue,
    date: String,
    resultDate: LocalDateTime,
    onEvent: (FamilyTasksScreenEvent) -> Unit,
    selectedTaskForChangeDateUrl: MutableState<Pair<String, Long>>
) {
    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)

                .clickable {
                    val initDate = if (resultDate > LocalDateTime.now()) {
                        resultDate.toEpochSecond(ZoneOffset.UTC) * 1000
                    } else {
                        LocalDateTime
                            .now()
                            .toEpochSecond(ZoneOffset.UTC) * 1000
                    }
                    selectedTaskForChangeDateUrl.value = Pair(task.url, initDate)
                    onEvent(FamilyTasksScreenEvent.ChangeStateDateDialog(true))
                }
                .padding(4.dp)
        ) {
            Text(
                text = date,
                fontSize = 14.sp,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = null,
            )
        }
    }
}


@Preview
@Composable
private fun DemoCardTaskView() {
    RastilkaTheme {
        Column {

            CardTaskView(
                task = SupportPreview.task,
                familyMembers = SupportPreview.listFamily,
                userId = "",
                isVisibleDescription = remember {
                    mutableStateOf(false)
                },
                modifier = Modifier,
                onEvent = {},
                selectedTaskForChangeDateUrl = remember { mutableStateOf(Pair("", 0L)) },
            )
            Spacer(modifier = Modifier.height(20.dp))
            CardTaskView(
                task = SupportPreview.task.copy(
                    value = SupportPreview.task.value.copy(
                        photo = null
                    )
                ),
                familyMembers = SupportPreview.listFamily,
                userId = "",
                isVisibleDescription = remember {
                    mutableStateOf(true)
                },
                modifier = Modifier,
                onEvent = {},
                selectedTaskForChangeDateUrl = remember { mutableStateOf(Pair("", 0L)) },
            )
            Spacer(modifier = Modifier.height(20.dp))
            CardTaskView(
                task = SupportPreview.task,
                familyMembers = SupportPreview.listFamily,
                userId = "",
                isVisibleDescription = remember {
                    mutableStateOf(true)
                },
                modifier = Modifier,
                onEvent = {},
                selectedTaskForChangeDateUrl = remember { mutableStateOf(Pair("", 0L)) },
            )
        }
    }
}