package com.rastilka.presentation.screens.create_task_screen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.rastilka.common.Resource
import com.rastilka.common.app_data.LoadingState
import com.rastilka.common.app_data.TypeId
import com.rastilka.domain.use_case.CreateTaskOrWishUseCase
import com.rastilka.domain.use_case.GetUserBySessionKeyUseCase
import com.rastilka.presentation.navigation.navigation_models.AppNavGraph
import com.rastilka.presentation.navigation.navigation_models.NavigationScreens
import com.rastilka.presentation.screens.create_task_screen.data.CreateTaskEvent
import com.rastilka.presentation.screens.create_task_screen.data.CreateTaskState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val createTaskOrWishUseCase: CreateTaskOrWishUseCase,
    private val getUserBySessionKeyUseCase: GetUserBySessionKeyUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CreateTaskState())
    val state get() = _state.asStateFlow()

    init {
        init()
    }

    fun onEvent(event: CreateTaskEvent) {
        when (event) {
            is CreateTaskEvent.ChangeTitle -> {
                _state.value = state.value.copy(
                    titleText = event.text
                )
            }

            is CreateTaskEvent.CreateTask -> {
                createTask(event.navController, event.uri)
            }

            is CreateTaskEvent.ChangeCountPrice -> {
                _state.value = state.value.copy(
                    countPrice = event.countPrice
                )
            }
        }
    }

    private fun init() {
        _state.value = state.value.copy(
            loadingState = LoadingState.Loading
        )
        viewModelScope.launch {
            when (val resourceUser = getUserBySessionKeyUseCase.invoke()) {
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        loadingState = LoadingState.FailedLoad,
                        errorMessage = resourceUser.message ?: ""
                    )
                }
                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        loadingState = LoadingState.Loading
                    )
                }
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        user = resourceUser.data,
                        loadingState = LoadingState.SuccessfulLoad
                    )
                }
            }
        }
    }

    private fun createTask(navController: NavController, imageUri: Uri?) {
        _state.value = state.value.copy(
            loadingState = LoadingState.Loading
        )
        viewModelScope.launch {
            when (val resource = createTaskOrWishUseCase.invoke(
                    typeId = TypeId.task.name,
                    lastUrl = "${state.value.user?.id}_tasks",
                    price = state.value.countPrice,
                    h1 = state.value.titleText,
                    picture = imageUri
                )
            ) {
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        loadingState = LoadingState.FailedLoad,
                        errorMessage = resource.message ?: ""
                    )
                }
                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        loadingState = LoadingState.Loading
                    )
                }
                is Resource.Success -> {
                    navController.navigate(AppNavGraph.FamilyTasksGraph.rout) {
                        popUpTo(NavigationScreens.FamilyTasksScreen.rout) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}