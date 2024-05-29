package com.rastilka.presentation.screens.family_tasks_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rastilka.R
import com.rastilka.common.utilits_support_preview.SupportPreview
import com.rastilka.presentation.components_app.app_components.FamilyMemberView
import com.rastilka.presentation.screens.family_tasks_screen.data.FamilyTasksScreenEvent
import com.rastilka.presentation.screens.family_tasks_screen.data.FamilyTasksScreenState
import com.rastilka.presentation.ui.theme.RastilkaTheme

@Composable
internal fun LazyRowFamilyMembersTasksView(
    state: State<FamilyTasksScreenState>,
    onEvent: (FamilyTasksScreenEvent) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(start = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp))
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            IconButton(
                onClick = {
                    onEvent(
                        FamilyTasksScreenEvent.ChangeFilterDate(
                            state = !state.value.filterDateNow
                        )
                    )
                },
                modifier = Modifier
                    .padding(end = 4.dp)
                    .border(
                        width = 2.dp,
                        shape = CircleShape,
                        color = if (state.value.filterDateNow) {
                            Color.Transparent
                        } else MaterialTheme.colorScheme.primary
                    )
                    .size(55.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_clock),
                    contentDescription = "Filter Time",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        items(state.value.familyMembers) { familyMember ->
            FamilyMemberView(
                familyMember = familyMember,
                isSelected = familyMember.id == state.value.filterUserId,
                size = 55.dp,
                isVisibleNameUser = false,
                select = {
                    onEvent(
                        FamilyTasksScreenEvent.GetFilteredTasksByFamilyMembers(
                            userId = familyMember.id
                        )
                    )
                },
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = android.graphics.Color.LTGRAY.toLong())
@Composable
private fun DemoLazyRowFamilyMembersTasksView(){
    RastilkaTheme {
        LazyRowFamilyMembersTasksView(
            state = remember {
                mutableStateOf(FamilyTasksScreenState(
                    familyMembers = SupportPreview.listFamily
                ))
            },
            onEvent = {}
        )
    }
}