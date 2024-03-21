package com.rastilka.presentation.screens.create_task_screen.data

import android.net.Uri
import androidx.navigation.NavController

sealed class CreateTaskEvent {
    data class ChangeTitle(val text: String) : CreateTaskEvent()

    data class ChangeCountPrice(val countPrice: String) : CreateTaskEvent()

    data class CreateTask(
        val navController: NavController,
        val uri: Uri?
    ) : CreateTaskEvent()
}