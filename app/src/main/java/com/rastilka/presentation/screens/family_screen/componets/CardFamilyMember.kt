package com.rastilka.presentation.screens.family_screen.componets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.rastilka.domain.models.User
import com.rastilka.presentation.components_app.clean_focus_keyboard.clearFocusOnKeyboardDismiss
import com.rastilka.presentation.components_app.coil_image_view.ImageLoadCoil
import com.rastilka.presentation.screens.family_screen.data.FamilyScreenEvent
import com.rastilka.presentation.ui.theme.RastilkaTheme

@Composable
fun CardFamilyMember(
    familyMember: User,
    userId: String,
    onEvent: (FamilyScreenEvent) -> Unit,
) {
    var point by remember { mutableStateOf("") }
    val pattern = remember { Regex("^\\d+\$") }
    val focus = remember { mutableStateOf(false) }
    var expandedDropDownMenu by remember { mutableStateOf(false) }
    val maxChar = 4

    Card(
        elevation = CardDefaults.elevatedCardElevation(0.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                ImageLoadCoil(
                    model = familyMember.picture.toString(),
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text(
                            text = familyMember.name,
                            textAlign = TextAlign.Start,
                            maxLines = 1,
                            fontSize = 20.sp,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(start = 12.dp)
                        )
                        if (familyMember.id != userId) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(
                                    onClick = { expandedDropDownMenu = !expandedDropDownMenu },
                                    modifier = Modifier.size(40.dp).align(Alignment.End)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_dotes),
                                        contentDescription = null
                                    )
                                }
                                DropdownMenu(
                                    expanded = expandedDropDownMenu,
                                    onDismissRequest = {
                                        expandedDropDownMenu = !expandedDropDownMenu
                                    }
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
                                                FamilyScreenEvent.DeleteMemberFamily(
                                                    deleteUserId = familyMember.id
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.height(40.dp)
                    ) {
                        IconButton(onClick = {
                            if (point.isNotEmpty()) {
                                onEvent(
                                    FamilyScreenEvent.GetUserPoint(
                                        fromUserId = familyMember.id,
                                        point = point.toLong()
                                    )
                                )
                            }
                            point = ""
                        }) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_minus),
                                contentDescription = null,
                            )
                        }
                        BasicTextField(
                            value = point,
                            onValueChange = { newText ->
                                if (newText.isEmpty() || newText.matches(pattern)) {
                                    point = newText.take(maxChar)
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
                                if (point.isEmpty() && !focus.value) {
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
                            onClick = {
                                if (point.isNotEmpty()) {
                                    onEvent(
                                        FamilyScreenEvent.AddUserPoint(
                                            toUserId = familyMember.id,
                                            point = point.toLong()
                                        )
                                    )
                                }
                                point = ""
                            }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_plus),
                                contentDescription = null,
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = familyMember.canUsePoints.toString(),
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
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun DemoFamilyCard() {
    RastilkaTheme {
        CardFamilyMember(
            familyMember = SupportPreview.user,
            userId = "",
            onEvent = {},
        )
    }
}
