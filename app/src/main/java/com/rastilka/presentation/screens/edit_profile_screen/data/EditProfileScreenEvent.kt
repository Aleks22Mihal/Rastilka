package com.rastilka.presentation.screens.edit_profile_screen.data

sealed class EditProfileScreenEvent {

    data object Refresh: EditProfileScreenEvent()

    data class EditPhoto(
        val pathPhoto: String?,
    ): EditProfileScreenEvent()

    data class EditName(
        val editName: String,
    ): EditProfileScreenEvent()

    data class EditEmail(
        val editEmail: String,
    ): EditProfileScreenEvent()

    data class EditPassword(
        val editPassword: String? = null,
    ): EditProfileScreenEvent()

    data object EditUserAndPassword : EditProfileScreenEvent()
}