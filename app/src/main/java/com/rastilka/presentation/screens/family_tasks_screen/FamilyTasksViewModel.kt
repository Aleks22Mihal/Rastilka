package com.rastilka.presentation.screens.family_tasks_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastilka.common.Resource
import com.rastilka.common.app_data.LoadingState
import com.rastilka.common.app_data.TypeIdForApi
import com.rastilka.domain.use_case.ChangeIndexUseCase
import com.rastilka.domain.use_case.DeleteTaskOrWishUseCase
import com.rastilka.domain.use_case.EditTaskOrWishUseCase
import com.rastilka.domain.use_case.GetFamilyMembersUseCase
import com.rastilka.domain.use_case.GetPointsUseCase
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class FamilyTasksViewModel @Inject constructor(
    private val getFamilyMembers: GetFamilyMembersUseCase,
    private val getTasksUseCase: GetTasksOrWishesUseCase,
    private val getUserBySessionKeyUseCase: GetUserBySessionKeyUseCase,
    private val setDidResponsibleUser: SetDidResponsibleUser,
    private val addResponsibleUser: SetResponsibleUser,
    private val deleteTask: DeleteTaskOrWishUseCase,
    private val editTaskUseCase: EditTaskOrWishUseCase,
    private val sendPointsUseCase: SendPointsUseCase,
    private val getPointsUseCase: GetPointsUseCase,
    private val changeIndexUseCase: ChangeIndexUseCase
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

            is FamilyTasksScreenEvent.GetPoint -> {
                getPoint(event.usersId, event.points, event.productUrl, event.title)
            }

            is FamilyTasksScreenEvent.MoveItemInList -> {
                moveItemInList(event.indexFrom, event.indexTo)
            }

            is FamilyTasksScreenEvent.ChangeLocationItemInList -> {
                changeLocationItemInList(urlTo = event.urlTo, urlFrom = event.urlFrom)
            }

            is FamilyTasksScreenEvent.ChangeStateDateDialog -> {
                changeStateDateDialog(event.state)
            }

            is FamilyTasksScreenEvent.ChangeFilterDate -> {
                changeFilterDate(event.state)
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
                _state.value = state.value.copy(
                    tasksList = resourceListTasks.data ?: emptyList(),
                    familyMembers = resourceFamilyMembers.data ?: emptyList(),
                    user = resourceUser.data,
                    filterUserId = state.value.filterUserId.ifEmpty { resourceUser.data?.id ?: "" },
                    initLoadingState = LoadingState.SuccessfulLoad
                )

                getFilterTasks()

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

    private fun changeFilterDate(value: Boolean) {
        _state.value = state.value.copy(filterDateNow = value)
        getFilterTasks()
    }

    private fun changeStateDateDialog(value: Boolean) {
        _state.value = state.value.copy(
            datePickerDialog = value
        )
    }

    private fun getFilterTasks() {

        val filterTaskByUser = state.value.tasksList.filter { task ->
            state.value.filterUserId in task.uuid.forUsers
        }

        val filterTaskByDate = filterTaskByUser.filter { task ->

            val apiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val resultDate = LocalDate.parse(task.value.date, apiFormat)

            if (state.value.filterDateNow) {
                resultDate <= LocalDate.now()
            } else LocalDate.now() < resultDate
        }

        _state.value = state.value.copy(
            filterTasks = if (!state.value.filterDateNow) {
                filterTaskByDate.sortedBy { task ->
                    task.value.date
                }
            }else filterTaskByDate
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
        _state.value = state.value.copy(
            filterUserId = userId
        )
        getFilterTasks()
    }

    /*
    * Берем нашу таску к которой хотим добавить отвественного
    * Если у нас один юзер есть и его id совпадает нечего не делаем
    * Иначе если один юзер и его id не совпадает удаляем старого и добавляем нового
    * Иначе прогоняем цикл чтобы удалить всех пользователей и добавить нового
    *
    * Почему так в одной из версий растилок был список отвественныхЮ сейчас только один
    *
    */
    private fun setResponsibleUser(
        productUrl: String,
        userId: String,
    ) {

        _state.value = state.value.copy(
            loadingState = LoadingState.Loading
        )

        viewModelScope.launch {
            val task = state.value.tasksList.find { tasks ->
                productUrl in tasks.value.url
            }

            if (task != null) {
                if (task.uuid.forUsers.size == 1 && userId in task.uuid.forUsers) {
                    _state.value = state.value.copy(
                        loadingState = LoadingState.SuccessfulLoad
                    )
                } else if (task.uuid.forUsers.size == 1 && userId !in task.uuid.forUsers) {
                    when (val resource =
                        addResponsibleUser.invoke(productUrl, task.uuid.forUsers.first())) {
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
                            when (val resourceAdd = addResponsibleUser.invoke(productUrl, userId)) {
                                is Resource.Error -> {
                                    _state.value = state.value.copy(
                                        loadingState = LoadingState.FailedLoad,
                                        errorMessage = resourceAdd.message ?: ""
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
                } else {
                    task.uuid.forUsers.forEach { tasksUserId ->
                        when (val resource = addResponsibleUser.invoke(productUrl, tasksUserId)) {
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                    errorMessage = resource.message ?: ""
                                )
                                return@forEach
                            }

                            is Resource.Loading -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.Loading
                                )
                            }

                            is Resource.Success -> {}
                        }
                    }.also {
                        when (val resourceAdd = addResponsibleUser.invoke(productUrl, userId)) {
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                    errorMessage = resourceAdd.message ?: ""
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
            }
        }
    }

    private fun deleteTask(productUrl: String) {
        viewModelScope.launch {
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

    private fun sendPoint(
        usersId: List<String>,
        points: String,
        productUrl: String,
        title: String,
    ) {
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
                        when (val resource =
                            sendPointsUseCase.invoke(usersId, points.toInt(), title)) {
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                    errorMessage = resource.message ?: ""
                                )
                                cancel()
                            }

                            is Resource.Loading -> {
                                _state.value =
                                    state.value.copy(loadingState = LoadingState.Loading)
                            }

                            is Resource.Success -> {
                                _state.value =
                                    state.value.copy(loadingState = LoadingState.SuccessfulLoad)
                            }
                        }
                    }.also {
                        when (
                            val resourceListTasks =
                                getTasksUseCase.invoke(type = TypeIdForApi.tasks)
                        ) {
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                    errorMessage = resourceListTasks.message ?: ""
                                )
                            }

                            is Resource.Loading -> {
                                _state.value =
                                    state.value.copy(loadingState = LoadingState.Loading)
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

    private fun getPoint(
        usersId: List<String>,
        points: String,
        productUrl: String,
        title: String
    ) {
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
                        when (val resource =
                            getPointsUseCase.invoke(usersId, points.toInt(), title)) {
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                    errorMessage = resource.message ?: ""
                                )
                                return@forEach
                            }

                            is Resource.Loading -> {
                                _state.value =
                                    state.value.copy(loadingState = LoadingState.Loading)
                            }

                            is Resource.Success -> {
                                _state.value =
                                    state.value.copy(loadingState = LoadingState.SuccessfulLoad)
                            }
                        }
                    }.also {
                        when (
                            val resourceListTasks =
                                getTasksUseCase.invoke(type = TypeIdForApi.tasks)
                        ) {
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                    errorMessage = resourceListTasks.message ?: ""
                                )
                            }

                            is Resource.Loading -> {
                                _state.value =
                                    state.value.copy(loadingState = LoadingState.Loading)
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

    private fun moveItemInList(from: Int, to: Int) {
        _state.value = state.value.copy(
            filterTasks = state.value.filterTasks.toMutableList().apply {
                add(to, removeAt(from))
            }
        )
    }

    private fun changeLocationItemInList(
        typeId: TypeIdForApi = TypeIdForApi.tasks,
        urlFrom: String,
        urlTo: String
    ) {
        _state.value = state.value.copy(
            loadingState = LoadingState.Loading,
            //initLoadingState = LoadingState.Loading
        )
        viewModelScope.launch {
            when (val resource = changeIndexUseCase(typeId, urlFrom, urlTo)) {
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        loadingState = LoadingState.FailedLoad,
                        errorMessage = resource.message ?: ""
                    )
                }

                is Resource.Loading -> {
                    _state.value =
                        state.value.copy(loadingState = LoadingState.Loading)
                }

                is Resource.Success -> {
                    when (val resourceListTasks = getTasksUseCase(type = TypeIdForApi.tasks)) {
                        is Resource.Success -> {

                            _state.value = state.value.copy(
                                loadingState = LoadingState.SuccessfulLoad,
                                //  initLoadingState = LoadingState.SuccessfulLoad,
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
}