package com.rastilka.presentation.screens.family_wishes_screen.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import com.rastilka.domain.models.User
import com.rastilka.presentation.components_app.app_components.FamilyMemberView
import com.rastilka.presentation.components_app.coil_image_view.ImageLoadCoil
import com.rastilka.presentation.ui.theme.RastilkaTheme

@Composable
fun CardWishView(
    wish: TaskOrWish,
    listFamilyMembers: List<User>,
    addResponsibleUser: (userId: String, activeUserId: List<String>) -> Unit,
    getPoints: (points: Int) -> Unit,
    deleteMemberFamily: () -> Unit
) {

    var priceText by remember { mutableStateOf(wish.value.salePrice ?: "1") }
    var expandedDropDownMenu by remember { mutableStateOf(false) }

    val price = wish.float.price ?: 0f
    val assembly = wish.value.assembly?.toFloat() ?: 0f

    val animationColorProgress by animateColorAsState(
        targetValue = if (assembly / price > 1f) {
            Color.Green
        } else MaterialTheme.colorScheme.primary,
        label = ""
    )

    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                Column {
                    IconButton(onClick = { expandedDropDownMenu = !expandedDropDownMenu }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_density_medium_24),
                            tint = MaterialTheme.colorScheme.tertiary,
                            contentDescription = null,
                        )
                    }
                    DropdownMenu(
                        expanded = expandedDropDownMenu,
                        onDismissRequest = { expandedDropDownMenu = !expandedDropDownMenu },
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
                            onClick = { deleteMemberFamily() },
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                ImageLoadCoil(
                    model = wish.value.photo.toString(),
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(20)),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = wish.value.h1,
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .weight(1f)
                )
                if (wish.uuid.forUsers.isNotEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
                                getPoints(priceText.toInt())
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_heart_delete_24),
                                contentDescription = "Send point"
                            )
                        }
                    }
                }
            }
            ActiveUserView(
                user = listFamilyMembers.find { familyMember ->
                    familyMember.id in wish.uuid.forUsers
                }
            )
            Text(
                text = "${wish.value.assembly ?: 0}/${wish.value.price ?: 0}",
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            )
            LinearProgressIndicator(
                progress = {
                    assembly / price
                },
                color = animationColorProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(10.dp)
            ) {
                items(
                    items = listFamilyMembers,
                    key = { familyMember -> familyMember.id }) { familyMember ->
                    FamilyMemberView(
                        familyMember = familyMember,
                        isSelected = familyMember.id in wish.uuid.forUsers,
                        select = {
                            addResponsibleUser(familyMember.id, wish.uuid.forUsers)
                        },
                        isVisibleNameUser = false,
                        size = 40.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun ActiveUserView(user: User?) {
    if (user == null) {
        Spacer(modifier = Modifier.size(50.dp))
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp)
        ) {
            ImageLoadCoil(
                model = user.picture.toString(),
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(100)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Text(
                text = user.canUsePoints.toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DemoCardWishView() {
    RastilkaTheme {
        CardWishView(
            wish = SupportPreview.task,
            listFamilyMembers = SupportPreview.listFamily,
            addResponsibleUser = { _, _ -> },
            getPoints = { _ -> },
            deleteMemberFamily = {}
        )
    }
}
