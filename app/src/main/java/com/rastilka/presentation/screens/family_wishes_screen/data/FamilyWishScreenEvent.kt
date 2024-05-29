package com.rastilka.presentation.screens.family_wishes_screen.data

sealed class FamilyWishScreenEvent {
    data object Refresh : FamilyWishScreenEvent()

    data class AddResponsibleUser(
        val productUrl: String,
        val userId: String,
    ) : FamilyWishScreenEvent()

    data class DeleteWish(
        val productUrl: String
    ) : FamilyWishScreenEvent()

    data class GetPoint(
       val fromUserId: String,
       val points: Long,
       val comment: String,
       val productUrl: String,
       val assembly: String
    ) : FamilyWishScreenEvent()

    data class SetPoint(
        val fromUserId: String,
        val points: Long,
        val comment: String,
        val productUrl: String,
        val assembly: String
    ) : FamilyWishScreenEvent()
}
