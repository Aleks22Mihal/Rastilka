package com.rastilka.presentation.screens.family_screen.data

sealed class FamilyScreenEvent {
    data object Refresh : FamilyScreenEvent()

    data class OpenBottomSheet(
        val isOpenBottomSheet: Boolean,
    ) : FamilyScreenEvent()

    data class AddFamilyMember(
        val addUserId: String,
    ) : FamilyScreenEvent()

    data class DeleteMemberFamily(
        val deleteUserId: String,
    ) : FamilyScreenEvent()

    data class AddUserPoint(
        val toUserId: String,
        val point: Long,
    ) : FamilyScreenEvent()

    data class GetUserPoint(
        val fromUserId: String,
        val point: Long,
    ) : FamilyScreenEvent()

    data class OpenAndCloseErrorDialog(
        val isOpen: Boolean
    ) : FamilyScreenEvent()
}
