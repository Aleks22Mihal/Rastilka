package com.rastilka.presentation.components_app.app_components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rastilka.common.utilits_support_preview.SupportPreview
import com.rastilka.domain.models.User
import com.rastilka.presentation.components_app.coil_image_view.ImageLoadCoil
import com.rastilka.presentation.ui.theme.RastilkaTheme

@Composable
fun FamilyMemberView(
    familyMember: User,
    isSelected: Boolean,
    select: () -> Unit,
    isVisibleNameUser: Boolean = true,
    size: Dp
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(end = 4.dp)
            .width(size)

    ) {
            ImageLoadCoil(
                model = familyMember.picture.toString(),
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .border(
                        width = 4.dp,
                        shape = MaterialTheme.shapes.extraLarge,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                    )
                    .size(size)
                    .clickable { select() },
                contentDescription = "Avatar Person",
                contentScale = ContentScale.Crop
            )
        if (isVisibleNameUser) {
            Text(
                text = familyMember.name,
                fontSize = 12.sp,
                fontWeight = FontWeight(400),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DemoFamilyMemberCard2() {
    RastilkaTheme {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            FamilyMemberView(
                familyMember = SupportPreview.user,
                isSelected = false,
                select = {},
                size = 80.dp
            )
            FamilyMemberView(
                familyMember = SupportPreview.user2,
                isSelected = true,
                select = {},
                size = 80.dp
            )
            FamilyMemberView(
                familyMember = SupportPreview.user2,
                isSelected = true,
                select = {},
                isVisibleNameUser = false,
                size = 80.dp
            )
        }
    }
}
