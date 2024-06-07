package com.rastilka.presentation.screens.login_screen.data

sealed class LoginScreenEvent {

    data object Refresh : LoginScreenEvent()

    data class EditEmail(
        val editEmail: String
    ) : LoginScreenEvent()

    data class EditPassword(
        val editPassword: String
    ) : LoginScreenEvent()

    data class VisiblePassword(
        val isVisible: Boolean
    ) : LoginScreenEvent()

    data class EditNameRegistration(
        val editName: String
    ) : LoginScreenEvent()

    data class EditEmailRegistration(
        val editEmail: String
    ) : LoginScreenEvent()

    data class EditPasswordRegistration(
        val editPassword: String
    ) : LoginScreenEvent()

    data class VisiblePasswordRegistration(
        val isVisible: Boolean
    ) : LoginScreenEvent()

    data object LogIn : LoginScreenEvent()

    data object LogOut : LoginScreenEvent()

    data object Registration : LoginScreenEvent()

    data class EditEmailForgetPassword(
        val editEmail: String
    ) : LoginScreenEvent()

    data class VisibleForgetPasswordDialog(
        val isVisible: Boolean
    ) : LoginScreenEvent()

    data object ConfirmationForgetPassword : LoginScreenEvent()
}
