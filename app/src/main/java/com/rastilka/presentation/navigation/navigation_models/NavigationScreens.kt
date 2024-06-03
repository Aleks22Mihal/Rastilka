package com.rastilka.presentation.navigation.navigation_models

sealed class NavigationScreens(val rout: String) {

    data object FamilyScreen : NavigationScreens(FAMILY_SCREEN_ROUT)
    data object FamilyTasksScreen : NavigationScreens(FAMILY_SCREEN_TASK_ROUT)
    data object WishScreen : NavigationScreens(WISH_SCREEN_ROUT)
    data object ScoresScreen : NavigationScreens(SCORE_SCREEN_ROUT)
    data object ProfileScreen : NavigationScreens(PROFILE_SCREEN_ROUT)
    data object EditProfileScreen : NavigationScreens(EDIT_PROFILE_SCREEN_ROUT)
    data object CreateTaskScreen : NavigationScreens(CREATE_TASK_SCREEN_ROUT)
    data object CreateWishScreen : NavigationScreens(CREATE_WISH_SCREEN_ROUT)

    data object TechnicalSupportScreen : NavigationScreens(TECHNICAL_SUPPORT_SCREEN_ROUT)

    fun withArgs(vararg args: String?): String {
        return buildString {
            append(rout)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    private companion object {
        const val FAMILY_SCREEN_ROUT = "Family_screen_rout"
        const val FAMILY_SCREEN_TASK_ROUT = "Family_task_screen_rout"
        const val WISH_SCREEN_ROUT = "Wish_screen_rout"
        const val SCORE_SCREEN_ROUT = "Score_screen_rout"
        const val PROFILE_SCREEN_ROUT = "Profile_screen_rout"
        const val EDIT_PROFILE_SCREEN_ROUT = "Edit_profile_screen_rout"
        const val CREATE_TASK_SCREEN_ROUT = "Create_task_screen_rout"
        const val CREATE_WISH_SCREEN_ROUT = "Create_wish_screen_rout"
        const val TECHNICAL_SUPPORT_SCREEN_ROUT = "Technical_support_screen_rout"
    }
}
