package com.rastilka.presentation.navigation.navigation_models

sealed class AppNavGraph(val rout: String) {

    data object FamilyGraph : AppNavGraph(FAMILY_ROUT)
    data object FamilyTasksGraph : AppNavGraph(FAMILY_TASK_ROUT)
    data object WishesGraph : AppNavGraph(WISH_ROUT)
    data object ScoresGraph : AppNavGraph(SCORE_ROUT)
    data object ProfileGraph : AppNavGraph(PROFILE_ROUT)

    private companion object {
        const val FAMILY_ROUT = "Family_rout"
        const val FAMILY_TASK_ROUT = "Family_task_rout"
        const val WISH_ROUT = "Wish_rout"
        const val SCORE_ROUT = "Score_rout"
        const val PROFILE_ROUT = "Profile_rout"
    }
}
