package com.rastilka.presentation.screens.family_wishes_screen.data

sealed class FamilyWishScreenEvent {
    data object Refresh : FamilyWishScreenEvent()

    data class AddResponsibleUser(
        val productUrl: String,
        val userId: String,
        val activeUserId: List<String>
    ) : FamilyWishScreenEvent()

    data class DeleteWish(
        val productUrl: String
    ) : FamilyWishScreenEvent()

    data class GetPoint(
       val fromUserId: String,
       val points: Int,
       val comment: String,
       val productUrl: String,
       val assembly: String
    ) : FamilyWishScreenEvent()
}
