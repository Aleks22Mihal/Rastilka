package com.rastilka.presentation.screens.family_tasks_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
import com.rastilka.domain.models.User
import com.rastilka.presentation.components_app.coil_image_view.ImageLoadCoil
import com.rastilka.presentation.ui.theme.RastilkaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardTaskView(
    task: TaskOrWish,
    delete: (String) -> Unit,
    familyMembers: List<User>,
    didResponsibleUser: (String) -> Unit,
    setResponsibleUser: (String) -> Unit,
    editTask: (property: String, value: String) -> Unit,
    sendPoint: (usersId: List<String>, points: String, title: String) -> Unit,
    getPoint: (usersId: List<String>, points: String, title: String) -> Unit,
) {

    var isVisibleDescription by remember {
        mutableStateOf(false)
    }
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
                delete(task.value.url)
                true
            } else false
        }
    )

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
                label = ""
            )
            val scale by animateFloatAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> 1.5f
                    SwipeToDismissBoxValue.StartToEnd -> 0f
                    SwipeToDismissBoxValue.Settled -> 1f
                },
                label = ""
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
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
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isVisibleDescription = !isVisibleDescription
                    if (!isVisibleDescription) {
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
                        didResponsibleUser = didResponsibleUser,
                    )
                    AnimatedVisibility(visible = !isVisibleDescription) {
                        Text(
                            text = task.value.h1,
                            fontSize = 14.sp,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    if (isVisibleDescription) {
                        Text(
                            text = "Готово",
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .clickable {
                                    if (task.value.h1 != title) {
                                        editTask("h1", title)
                                    } else {
                                        isVisibleDescription = !isVisibleDescription
                                        if (!isVisibleDescription) {
                                            title = task.value.h1
                                        }
                                    }
                                }
                        )
                    }
                }

                AnimatedVisibility(visible = isVisibleDescription) {

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = {
                                title = it
                            },
                            textStyle = TextStyle(fontSize = 14.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 10.dp)
                        )
                        if (task.value.photo != null) {
                            ImageLoadCoil(
                                model = task.value.photo,
                                modifier = Modifier.size(200.dp),
                                contentDescription = ""
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Назначить отвественного")
                        Spacer(modifier = Modifier.height(8.dp))
                        ResponsibleUserTaskView(
                            task = task,
                            familyMembers = familyMembers,
                            setResponsibleUser = setResponsibleUser
                        )
                        if (task.value.price != null && task.float.price != 0f) {
                            PriceTaskView(
                                task = task,
                                sendPoint = sendPoint,
                                tittle = title,
                                getPoint = getPoint
                            )
                        }
                    }
                }
            }
            HorizontalDivider()
        }
    }
}


@Composable
private fun DidResponsibleUserTaskView(
    task: TaskOrWish,
    familyMembers: List<User>,
    didResponsibleUser: (String) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(end = 4.dp)
    ) {
        familyMembers.forEach { familyMember ->
            if (familyMember.id in task.uuid.forUsers) {
                ImageLoadCoil(
                    model = familyMember.picture.toString(),
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraLarge)
                        .border(
                            width = 4.dp,
                            shape = MaterialTheme.shapes.extraLarge,
                            color = if (familyMember.id in task.uuid.didUsers) {
                                MaterialTheme.colorScheme.primary
                            } else Color.Transparent
                        )
                        .size(40.dp)
                        .clickable {
                            didResponsibleUser(familyMember.id)
                        },
                    contentDescription = "Avatar Person",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
private fun ResponsibleUserTaskView(
    task: TaskOrWish,
    familyMembers: List<User>,
    setResponsibleUser: (String) -> Unit,
) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(familyMembers) { familyMember ->
            ImageLoadCoil(
                model = familyMember.picture.toString(),
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .border(
                        width = 4.dp,
                        shape = MaterialTheme.shapes.extraLarge,
                        color = if (familyMember.id in task.uuid.forUsers) {
                            MaterialTheme.colorScheme.primary
                        } else Color.Transparent
                    )
                    .size(35.dp)
                    .clickable {
                        setResponsibleUser(familyMember.id)
                    },
                contentDescription = "Avatar Person",
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun PriceTaskView(
    task: TaskOrWish,
    tittle: String,
    sendPoint: (usersId: List<String>, points: String, title: String) -> Unit,
    getPoint: (usersId: List<String>, points: String, title: String) -> Unit,
) {
    var priceText by remember {
        mutableStateOf(task.value.price ?: "")
    }

    Spacer(modifier = Modifier.height(8.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {

        IconButton(
            enabled = priceText.isNotEmpty(),
            onClick = {
                getPoint(task.uuid.forUsers, priceText, tittle)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_heart_delete_24),
                contentDescription = "Send point"
            )
        }

        BasicTextField(
            value = priceText,
            onValueChange = {
                priceText = it.take(4)
            },
            textStyle = LocalTextStyle.current.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.border(width = 1.dp, color = Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    innerTextField()
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.NumberPassword
            ),
            modifier = Modifier.width(40.dp)
        )

        IconButton(
            enabled = priceText.isNotEmpty(),
            onClick = {
                sendPoint(task.uuid.forUsers, priceText, tittle)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_heart_plus_24),
                contentDescription = "Send point"
            )
        }
    }
}


@Preview
@Composable
private fun DemoCardTaskView() {
    RastilkaTheme {
        CardTaskView(
            task = SupportPreview.task,
            delete = {},
            familyMembers = SupportPreview.listFamily,
            didResponsibleUser = {},
            setResponsibleUser = {},
            editTask = { _, _ -> },
            sendPoint = { _, _, _ -> },
            getPoint = { _, _, _ -> },
        )
    }
}