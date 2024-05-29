package com.rastilka.presentation.screens.profile_screen.data

import com.rastilka.R
import com.rastilka.presentation.navigation.navigation_models.NavigationScreens

sealed class ProfileMenuButton(
    val iconDrawable: Int,
    val nameMenu: String,
    val navigationRout: String
){
    data object TechnicalSupport: ProfileMenuButton(
        iconDrawable = R.drawable.ic_teh_support,
        nameMenu = "Техническая поддрежка",
        navigationRout = NavigationScreens.TechnicalSupportScreen.rout
    )
    data object ProfileEditor: ProfileMenuButton(
        iconDrawable = R.drawable.ic_prof,
        nameMenu = "Аккаунт",
        navigationRout = NavigationScreens.EditProfileScreen.rout
    )
}
