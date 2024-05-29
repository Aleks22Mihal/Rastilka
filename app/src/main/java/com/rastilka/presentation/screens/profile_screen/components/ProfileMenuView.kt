package com.rastilka.presentation.screens.profile_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rastilka.R
import com.rastilka.presentation.screens.profile_screen.data.ProfileMenuButton
import com.rastilka.presentation.ui.theme.RastilkaTheme

@Composable
fun ProfileMenuView(
    listButtonMenu: List<ProfileMenuButton>,
    navController: NavController
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
    ) {
        Column {
            listButtonMenu.forEachIndexed { index, profileMenuButton ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        navController.navigate(route = profileMenuButton.navigationRout){
                            launchSingleTop = true
                        }
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = profileMenuButton.iconDrawable),
                            contentDescription = profileMenuButton.nameMenu
                        )
                        Text(text = profileMenuButton.nameMenu)
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_navigate_next_24),
                            contentDescription = null,
                            tint = Color.LightGray
                        )
                    }
                    if (index != listButtonMenu.lastIndex) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                        )
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
        val listButtonMenu: List<ProfileMenuButton> =
            listOf(ProfileMenuButton.ProfileEditor, ProfileMenuButton.TechnicalSupport)
        ProfileMenuView(
            listButtonMenu = listButtonMenu,
            navController = rememberNavController()
        )
    }
}