package com.rastilka.presentation.screens.family_wishes_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.rastilka.presentation.components_app.app_components.FamilyMemberView
import com.rastilka.presentation.components_app.clean_focus_keyboard.clearFocusOnKeyboardDismiss
import com.rastilka.presentation.components_app.coil_image_view.ImageLoadCoil
import com.rastilka.presentation.screens.family_wishes_screen.data.FamilyWishScreenEvent
import com.rastilka.presentation.ui.theme.RastilkaTheme

@Composable
fun CardWishView(
    wish: TaskOrWish,
    listFamilyMembers: List<User>,
    onEvent: (FamilyWishScreenEvent) -> Unit,
    isVisibleAdditionalContent: MutableState<Boolean> = remember { mutableStateOf(false) },
) {

    var expandedDropDownMenu by remember { mutableStateOf(false) }

    var priceText by remember { mutableStateOf(wish.value.salePrice ?: "1") }
    val focus = remember { mutableStateOf(false) }
    val pattern = remember { Regex("^\\d+\$") }
    val maxChar = 4

    Card(
        onClick = { isVisibleAdditionalContent.value = !isVisibleAdditionalContent.value },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            IconButton(
                onClick = { expandedDropDownMenu = !expandedDropDownMenu },
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.End)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_dotes),
                    contentDescription = null
                )

                DropdownMenu(
                    expanded = expandedDropDownMenu,
                    onDismissRequest = {
                        expandedDropDownMenu = !expandedDropDownMenu
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Удалить") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_delete_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        },
                        onClick = {
                            onEvent(
                                FamilyWishScreenEvent.DeleteWish(
                                    productUrl = wish.value.url
                                )
                            )
                        }
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                ActiveUserView(
                    user = listFamilyMembers.find { familyMember ->
                        familyMember.id in wish.uuid.forUsers
                    }
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = wish.value.h1)
                    Text(text = wish.value.assembly ?: "")
                }
                ImageLoadCoil(
                    model = wish.value.photo.toString(),
                    modifier = Modifier
                        .size(80.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            AnimatedVisibility(visible = isVisibleAdditionalContent.value) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LazyRow(
                        contentPadding = PaddingValues(start = 4.dp, end = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(
                            items = listFamilyMembers,
                            key = { familyMember -> familyMember.id }) { familyMember ->
                            FamilyMemberView(
                                familyMember = familyMember,
                                isSelected = familyMember.id in wish.uuid.forUsers,
                                select = {
                                    onEvent(
                                        FamilyWishScreenEvent.AddResponsibleUser(
                                            productUrl = wish.value.url,
                                            userId = familyMember.id
                                        )
                                    )
                                },
                                isVisibleNameUser = false,
                                size = 40.dp
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            enabled = listFamilyMembers.any { familyMember ->
                                familyMember.id in wish.uuid.forUsers
                            },
                            onClick = {
                                if (priceText.isNotEmpty()) {
                                    onEvent(
                                        FamilyWishScreenEvent.GetPoint(
                                            fromUserId = wish.uuid.forUsers.first(),
                                            points = priceText.toLong(),
                                            comment = wish.value.h1,
                                            productUrl = wish.value.url,
                                            assembly = wish.value.assembly ?: "0"
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
                            enabled = listFamilyMembers.any { familyMember ->
                                familyMember.id in wish.uuid.forUsers
                            },
                            onClick = {
                                if (priceText.isNotEmpty()) {
                                    onEvent(
                                        FamilyWishScreenEvent.SetPoint(
                                            fromUserId = wish.uuid.forUsers.first(),
                                            points = priceText.toLong(),
                                            comment = wish.value.h1,
                                            productUrl = wish.value.url,
                                            assembly = wish.value.assembly ?: "0"
                                        )
                                    )
                                }
                            }) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_plus),
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ActiveUserView(user: User?) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        ImageLoadCoil(
            model = user?.picture.toString(),
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(color = Color.LightGray),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = user?.canUsePoints?.toString() ?: "0",
                textAlign = TextAlign.Center,
                maxLines = 1,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
            )
            Image(
                painter = painterResource(id = R.drawable.logo_rastilka),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    }

}

@Preview
@Composable
private fun DemoCardWishView2() {
    RastilkaTheme {
        Column {
            CardWishView(
                wish = SupportPreview.task,
                onEvent = {},
                listFamilyMembers = SupportPreview.listFamily
            )

            Spacer(modifier = Modifier.height(10.dp))

            CardWishView(
                wish = SupportPreview.task,
                onEvent = {},
                isVisibleAdditionalContent = remember {
                    mutableStateOf(true)
                },
                listFamilyMembers = SupportPreview.listFamily
            )
        }
    }
}