package com.rastilka.presentation.screens.family_screen.views

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.rastilka.domain.models.User
import com.rastilka.presentation.components_app.clean_focus_keyboard.clearFocusOnKeyboardDismiss
import com.rastilka.presentation.components_app.coil_image_view.ImageLoadCoil
import com.rastilka.presentation.ui.theme.RastilkaTheme

@Composable
fun CardFamilyMember(
    familyMember: User,
    deleteMemberFamily: () -> Unit,
    sendPointMemberFamily: (Int) -> Unit,
    getPointMemberFamily: (Int) -> Unit,
) {
    var point by remember { mutableStateOf("") }
    val pattern = remember { Regex("^\\d+\$") }
    var expandedDropDownMenu by remember { mutableStateOf(false) }
    val maxChar = 4

    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(10.dp),
        modifier = Modifier.padding(10.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                ImageLoadCoil(
                    model = familyMember.picture.toString(),
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(100))
                        .background(Color.White),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = familyMember.name,
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 20.dp, end = 40.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Column {
                    IconButton(onClick = { expandedDropDownMenu = !expandedDropDownMenu }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_density_medium_24),
                            tint = MaterialTheme.colorScheme.tertiary,
                            contentDescription = null
                        )
                    }
                    DropdownMenu(
                        expanded = expandedDropDownMenu,
                        onDismissRequest = { expandedDropDownMenu = !expandedDropDownMenu }) {
                        DropdownMenuItem(
                            text = { Text(text = "Удалить") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_delete_24),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                            },
                            onClick = { deleteMemberFamily() }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
            ) {


                OutlinedTextField(
                    value = point,
                    onValueChange = { newText ->
                        if (newText.isEmpty() || newText.matches(pattern)) {
                            point = newText.take(maxChar)
                        }
                    },
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            if (point.isNotEmpty()) {
                                sendPointMemberFamily(point.toInt())
                            }
                            point = ""
                        }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_heart_plus_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    },
                    leadingIcon = {
                        IconButton(onClick = {
                            if (point.isNotEmpty()) {
                                getPointMemberFamily(point.toInt())
                            }
                            point = ""
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_heart_delete_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    },
                    shape = RoundedCornerShape(100),
                    modifier = Modifier
                        .width(150.dp)
                        .clearFocusOnKeyboardDismiss()
                )
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(
                        text = familyMember.canUsePoints.toString(),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = "Баллы",
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DemoFamilyCard() {
    RastilkaTheme {
        CardFamilyMember(
            familyMember = SupportPreview.user,
            deleteMemberFamily = {},
            sendPointMemberFamily = {},
            getPointMemberFamily = {},
        )
    }
}