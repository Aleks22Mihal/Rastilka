package com.rastilka.presentation.screens.family_tasks_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastilka.common.Resource
import com.rastilka.common.app_data.LoadingState
import com.rastilka.common.app_data.TypeIdForApi
import com.rastilka.domain.use_case.DeleteTaskOrWishUseCase
import com.rastilka.domain.use_case.EditTaskOrWishUseCase
import com.rastilka.domain.use_case.GetFamilyMembersUseCase
import com.rastilka.domain.use_case.GetTaskUseCase
import com.rastilka.domain.use_case.GetTasksOrWishesUseCase
import com.rastilka.domain.use_case.GetUserBySessionKeyUseCase
import com.rastilka.domain.use_case.SendPointsUseCase
import com.rastilka.domain.use_case.SetDidResponsibleUser
import com.rastilka.domain.use_case.SetResponsibleUser
import com.rastilka.presentation.screens.family_tasks_screen.data.FamilyTasksScreenEvent
import com.rastilka.presentation.screens.family_tasks_screen.data.FamilyTasksScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FamilyTasksViewModel @Inject constructor(
    private val getFamilyMembers: GetFamilyMembersUseCase,
    private val getTasksUseCase: GetTasksOrWishesUseCase,
    private val getUserBySessionKeyUseCase: GetUserBySessionKeyUseCase,
    private val setDidResponsibleUser: SetDidResponsibleUser,
    private val addResponsibleUser: SetResponsibleUser,
    private val deleteTask: DeleteTaskOrWishUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val editTaskUseCase: EditTaskOrWishUseCase,
    private val sendPointsUseCase: SendPointsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FamilyTasksScreenState())
    val state = _state.asStateFlow()

    init {
        init()
    }

    fun onEvent(event: FamilyTasksScreenEvent) {
        when (event) {

            is FamilyTasksScreenEvent.Refresh -> {
                init()
            }

            is FamilyTasksScreenEvent.GetFilteredTasksByFamilyMembers -> {
                setFilterUser(event.userId)
            }

            is FamilyTasksScreenEvent.SetDidResponsibleUser -> {
                setDidResponsibleUser(event.productUrl, event.userId)
            }

            is FamilyTasksScreenEvent.AddResponsibleUser -> {
                setResponsibleUser(event.productUrl, event.userId)
            }

            is FamilyTasksScreenEvent.DeleteTask -> {
                deleteTask(event.productUrl)
            }

            is FamilyTasksScreenEvent.EditTask -> {
                editTask(event.productUrl, event.property, event.value)
            }

            is FamilyTasksScreenEvent.SendPoint -> {
                sendPoint(event.usersId, event.points, event.productUrl, event.title)
            }
        }
    }


    private fun init() {
        viewModelScope.launch {

            _state.value = state.value.copy(initLoadingState = LoadingState.Loading)
            val resourceListTasks = getTasksUseCase.invoke(type = TypeIdForApi.tasks)
            val resourceFamilyMembers = getFamilyMembers.invoke()
            val resourceUser = getUserBySessionKeyUseCase.invoke()

            if (resourceFamilyMembers is Resource.Success &&
                resourceListTasks is Resource.Success &&
                resourceUser is Resource.Success
            ) {
                when (val resourceMainFolderTask =
                    getTaskUseCase.invoke(resourceUser.data!!.id + "_tasks")) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            initLoadingState = LoadingState.FailedLoad,
                            errorMessage = resourceListTasks.message ?: ""
                        )
                    }

                    is Resource.Loading -> {
                        _state.value = state.value.copy(initLoadingState = LoadingState.Loading)
                    }

                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            tasksList = resourceListTasks.data ?: emptyList(),
                            filterTasks = resourceListTasks.data ?: emptyList(),
                            familyMembers = resourceFamilyMembers.data ?: emptyList(),
                            user = resourceUser.data,
                            mainFolderTask = resourceMainFolderTask.data,
                            initLoadingState = LoadingState.SuccessfulLoad
                        )
                        getFilterTasks()
                    }
                }

            } else if (resourceFamilyMembers is Resource.Loading && resourceListTasks is Resource.Loading && resourceUser is Resource.Loading) {
                _state.value = state.value.copy(
                    initLoadingState = LoadingState.Loading
                )
            } else if (resourceFamilyMembers is Resource.Error || resourceListTasks is Resource.Error || resourceUser is Resource.Error) {
                _state.value = state.value.copy(
                    initLoadingState = LoadingState.FailedLoad,
                    errorMessage = resourceListTasks.message ?: ""
                )
            }
        }
    }

    private fun getFilterTasks() {
        _state.value = state.value.copy(
            filterTasks = state.value.tasksList.filter { task ->
                if (!state.value.mainFolderTask?.uuid?.forUsers.isNullOrEmpty()) {
                    state.value.mainFolderTask?.uuid?.forUsers!!.any {
                        it in task.uuid.forUsers
                    }
                } else task.uuid.forUsers.isEmpty()
            }
        )
    }

    private fun setDidResponsibleUser(productUrl: String, userId: String) {
        viewModelScope.launch {
            _state.value = state.value.copy(
                loadingState = LoadingState.Loading
            )
            when (val resource = setDidResponsibleUser.invoke(productUrl, userId)) {
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

                    when (val resourceListTasks = getTasksUseCase(type = TypeIdForApi.tasks)) {
                        is Resource.Success -> {

                            _state.value = state.value.copy(
                                loadingState = LoadingState.SuccessfulLoad,
                                tasksList = resourceListTasks.data ?: emptyList()
                            )
                            getFilterTasks()
                        }

                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                initLoadingState = LoadingState.Loading,
                            )
                        }

                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                loadingState = LoadingState.FailedLoad,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setFilterUser(userId: String) {
        viewModelScope.launch {
            _state.value = state.value.copy(
                loadingState = LoadingState.Loading
            )
            when (val resource = addResponsibleUser.invoke(
                userId = userId,
                productUrl = state.value.user!!.id + "_tasks"
            )) {
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        initLoadingState = LoadingState.FailedLoad,
                        errorMessage = resource.message ?: ""
                    )
                }

                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        loadingState = LoadingState.Loading
                    )
                }

                is Resource.Success -> {
                    when (val resourceMainFolderTask =
                        getTaskUseCase.invoke(state.value.user!!.id + "_tasks")) {
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                initLoadingState = LoadingState.FailedLoad,
                                errorMessage = resourceMainFolderTask.message ?: ""
                            )
                        }

                        is Resource.Loading -> {
                            _state.value = state.value.copy(loadingState = LoadingState.Loading)
                        }

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                mainFolderTask = resourceMainFolderTask.data,
                                loadingState = LoadingState.SuccessfulLoad
                            )
                            getFilterTasks()
                        }
                    }
                }
            }
        }
    }

    private fun setResponsibleUser(productUrl: String, userId: String) {
        viewModelScope.launch {
            _state.value = state.value.copy(
                loadingState = LoadingState.Loading
            )
            when (val resource = addResponsibleUser.invoke(productUrl, userId)) {
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
                    when (val resourceListTasks =
                        getTasksUseCase.invoke(type = TypeIdForApi.tasks)) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                loadingState = LoadingState.SuccessfulLoad,
                                tasksList = resourceListTasks.data ?: emptyList()
                            )
                            getFilterTasks()
                        }

                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                initLoadingState = LoadingState.Loading,
                            )
                        }

                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                loadingState = LoadingState.FailedLoad,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun deleteTask(productUrl: String) {
        viewModelScope.launch {
            Log.e("", "2222")
            _state.value = state.value.copy(
                loadingState = LoadingState.Loading
            )
            when (val resource = deleteTask.invoke(productUrl)) {
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
                    when (val resourceListTasks =
                        getTasksUseCase.invoke(type = TypeIdForApi.tasks)) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                loadingState = LoadingState.SuccessfulLoad,
                                tasksList = resourceListTasks.data ?: emptyList()
                            )
                            getFilterTasks()
                        }

                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                initLoadingState = LoadingState.Loading,
                            )
                        }

                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                loadingState = LoadingState.FailedLoad,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun editTask(productUrl: String, property: String, value: String) {
        viewModelScope.launch {
            _state.value = state.value.copy(loadingState = LoadingState.Loading)
            when (val resource = editTaskUseCase.invoke(productUrl, property, value)) {
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        loadingState = LoadingState.FailedLoad,
                        errorMessage = resource.message ?: ""
                    )
                }

                is Resource.Loading -> {
                    _state.value = state.value.copy(loadingState = LoadingState.Loading)
                }

                is Resource.Success -> {
                    when (
                        val resourceListTasks = getTasksUseCase.invoke(type = TypeIdForApi.tasks)
                    ) {
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                loadingState = LoadingState.FailedLoad,
                                errorMessage = resource.message ?: ""
                            )
                        }

                        is Resource.Loading -> {
                            _state.value = state.value.copy(loadingState = LoadingState.Loading)
                        }

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                loadingState = LoadingState.SuccessfulLoad,
                                tasksList = resourceListTasks.data ?: emptyList()
                            )
                            getFilterTasks()
                        }
                    }
                }
            }
        }
    }

    private fun sendPoint(usersId: List<String>, points: String, productUrl: String, title: String) {
        viewModelScope.launch {
            _state.value = state.value.copy(loadingState = LoadingState.Loading)
            when (val resourceChangePrice = editTaskUseCase.invoke(
                property = "price",
                value = points,
                productUrl = productUrl,
            )) {
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        loadingState = LoadingState.FailedLoad,
                        errorMessage = resourceChangePrice.message ?: ""
                    )
                }

                is Resource.Loading -> {
                    _state.value = state.value.copy(loadingState = LoadingState.Loading)
                }

                is Resource.Success -> {
                    usersId.forEach { usersId ->
                        when (val resource = sendPointsUseCase.invoke(usersId, points.toInt(), title)) {
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                    errorMessage = resource.message ?: ""
                                )
                                cancel()
                            }

                            is Resource.Loading -> {
                                _state.value = state.value.copy(loadingState = LoadingState.Loading)
                            }

                            is Resource.Success -> {
                                _state.value =
                                    state.value.copy(loadingState = LoadingState.SuccessfulLoad)
                            }
                        }
                    }.also {
                        when (
                            val resourceListTasks = getTasksUseCase.invoke(type = TypeIdForApi.tasks)
                        ) {
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                    errorMessage = resourceListTasks.message ?: ""
                                )
                            }

                            is Resource.Loading -> {
                                _state.value = state.value.copy(loadingState = LoadingState.Loading)
                            }

                            is Resource.Success -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.SuccessfulLoad,
                                    tasksList = resourceListTasks.data ?: emptyList()
                                )
                                getFilterTasks()
                            }
                        }
                    }
                }
            }
        }
    }
}