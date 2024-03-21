package com.rastilka.presentation.screens.profile_screen.data

import com.rastilka.R

sealed class ProfileMenuButton(
    val iconDrawable: Int,
    val nameMenu: String,
    val navigationRout: String
){
    data object TechnicalSupport: ProfileMenuButton(
        iconDrawable = R.drawable.ic_message_24,
        nameMenu = "Техническая поддрежка",
        navigationRout = ""
    )
    data object ProfileEditor: ProfileMenuButton(
        iconDrawable = R.drawable.ic_profile_24,
        nameMenu = "Профиль",
        navigationRout = ""
    )
}
