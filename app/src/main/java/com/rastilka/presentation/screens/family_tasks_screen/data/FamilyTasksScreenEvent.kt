package com.rastilka.presentation.screens.family_tasks_screen.data

sealed class FamilyTasksScreenEvent {

    data object Refresh : FamilyTasksScreenEvent()

    data class GetFilteredTasksByFamilyMembers(
        val userId: String
    ) : FamilyTasksScreenEvent()

    data class SetDidResponsibleUser(
        val productUrl: String,
        val userId: String,
    ) : FamilyTasksScreenEvent()

    data class AddResponsibleUser(
        val productUrl: String,
        val userId: String
    ) : FamilyTasksScreenEvent()

    data class DeleteTask(
        val productUrl: String
    ) : FamilyTasksScreenEvent()

    data class EditTask(
        val productUrl: String,
        val property: String,
        val value: String
    ) : FamilyTasksScreenEvent()

    data class SendPoint(
        val points: String,
        val usersId: List<String>,
        val productUrl: String,
        val title: String
    ) : FamilyTasksScreenEvent()
}