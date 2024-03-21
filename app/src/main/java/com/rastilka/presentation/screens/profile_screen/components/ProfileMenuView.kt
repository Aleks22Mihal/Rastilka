package com.rastilka.presentation.screens.profile_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rastilka.presentation.screens.profile_screen.data.ProfileMenuButton
import com.rastilka.presentation.ui.theme.RastilkaTheme

@Composable
fun ProfileMenuView() {
    val listButtonMenu: List<ProfileMenuButton> =
        listOf(ProfileMenuButton.TechnicalSupport, ProfileMenuButton.ProfileEditor)

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.padding(10.dp)
    ) {
        Column {
            listButtonMenu.forEachIndexed { index, profileMenuButton ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        /*TODO*/
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)

                    ) {
                        Icon(
                            painter = painterResource(id = profileMenuButton.iconDrawable),
                            contentDescription = profileMenuButton.nameMenu
                        )
                        Text(text = profileMenuButton.nameMenu)
                    }
                    if (index != listButtonMenu.lastIndex) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProfileMenuDemo() {
    RastilkaTheme {
        ProfileMenuView()
    }
}