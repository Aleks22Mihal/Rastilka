package com.rastilka.presentation.screens.create_task_screen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.rastilka.common.Resource
import com.rastilka.common.app_data.LoadingState
import com.rastilka.common.app_data.TypeId
import com.rastilka.domain.use_case.CreateTaskOrWishUseCase
import com.rastilka.domain.use_case.GetFamilyMembersUseCase
import com.rastilka.domain.use_case.GetUserBySessionKeyUseCase
import com.rastilka.presentation.navigation.navigation_models.AppNavGraph
import com.rastilka.presentation.navigation.navigation_models.NavigationScreens
import com.rastilka.presentation.screens.create_task_screen.data.CreateTaskEvent
import com.rastilka.presentation.screens.create_task_screen.data.CreateTaskState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val createTaskOrWishUseCase: CreateTaskOrWishUseCase,
    private val getUserBySessionKeyUseCase: GetUserBySessionKeyUseCase,
    private val getFamilyMembersUseCase: GetFamilyMembersUseCase,
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

            is CreateTaskEvent.OpenDatePickerDialog -> {
                _state.value = state.value.copy(isOpenDatePicker = event.isOpen)
            }

            is CreateTaskEvent.SetSelectedDate -> {
                _state.value = state.value.copy(dateMillis = event.date)
            }

            is CreateTaskEvent.SelectUserId -> {
                _state.value = state.value.copy(
                    selectedFamilyMemberId = event.selectedUserId
                )
            }
        }
    }

    private fun init() {
        _state.value = state.value.copy(
            loadingState = LoadingState.Loading
        )
        viewModelScope.launch {
            when (val resourceUser = getUserBySessionKeyUseCase()) {
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
                    when (val resourceFamilyMembers = getFamilyMembersUseCase()) {
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
                            val date = LocalDateTime.now()
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()

                            _state.value = state.value.copy(
                                user = resourceUser.data,
                                listFamilyMembers = resourceFamilyMembers.data ?: emptyList(),
                                selectedFamilyMemberId = resourceUser.data?.id ?: "",
                                dateMillis = date,
                                loadingState = LoadingState.SuccessfulLoad
                            )
                        }
                    }
                }
            }
        }
    }

    private fun createTask(navController: NavController, imageUri: Uri?) {
        _state.value = state.value.copy(
            loadingState = LoadingState.Loading
        )
        viewModelScope.launch {
            val apiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val install = Instant.ofEpochMilli(state.value.dateMillis)
            val date = LocalDateTime.ofInstant(install, ZoneId.systemDefault())
            val formatDate = date.format(apiFormat)

            when (
                val resource = createTaskOrWishUseCase(
                    typeId = TypeId.task.name,
                    lastUrl = "${state.value.user?.id}_tasks",
                    price = state.value.countPrice,
                    h1 = state.value.titleText,
                    forUserId = state.value.selectedFamilyMemberId,
                    date = formatDate,
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
