package com.rastilka.presentation.screens.family_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastilka.common.Resource
import com.rastilka.common.app_data.LoadingState
import com.rastilka.domain.use_case.AttachUserUseCase
import com.rastilka.domain.use_case.DetachUserUseCase
import com.rastilka.domain.use_case.GetFamilyMembersUseCase
import com.rastilka.domain.use_case.GetPointsUseCase
import com.rastilka.domain.use_case.GetUserBySessionKeyUseCase
import com.rastilka.domain.use_case.SendPointsUseCase
import com.rastilka.presentation.screens.family_screen.data.FamilyScreenEvent
import com.rastilka.presentation.screens.family_screen.data.FamilyScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FamilyViewModel @Inject constructor(
    private val getFamilyMembers: GetFamilyMembersUseCase,
    private val getUserBySessionKeyUseCase: GetUserBySessionKeyUseCase,
    private val attachUser: AttachUserUseCase,
    private val detachUser: DetachUserUseCase,
    private val getPoints: GetPointsUseCase,
    private val sendPoints: SendPointsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FamilyScreenState())
    val state = _state.asStateFlow()

    init {
        init()
    }

    fun onEvent(event: FamilyScreenEvent) {
        when (event) {

            FamilyScreenEvent.Refresh -> {
                init()
            }

            is FamilyScreenEvent.AddFamilyMember -> {
                addMemberFamily(addUserId = event.addUserId)
            }

            is FamilyScreenEvent.DeleteMemberFamily -> {
                deleteMemberFamily(deleteUserId = event.deleteUserId)
            }

            is FamilyScreenEvent.AddUserPoint -> {
                addUserPoint(toUserId = event.toUserId, point = event.point)
            }

            is FamilyScreenEvent.GetUserPoint -> {
                getUserPoint(fromUserId = event.fromUserId, point = event.point)
            }

            is FamilyScreenEvent.OpenBottomSheet -> {
                openBottomSheet(isOpen = event.isOpenBottomSheet)
            }

            is FamilyScreenEvent.OpenAndCloseErrorDialog -> {
                _state.value = state.value.copy(
                    errorText = null,
                    isOpenErrorDialog = event.isOpen
                )
            }
        }
    }

    private fun init() {
        _state.value = state.value.copy(initLoadingState = LoadingState.Loading)

        viewModelScope.launch {

            val resourceUser = getUserBySessionKeyUseCase()
            val resourceFamilyMembers = getFamilyMembers()

            if (resourceFamilyMembers is Resource.Success &&
                resourceUser is Resource.Success
            ) {
                _state.value = state.value.copy(
                    user = resourceUser.data,
                    familyList = resourceFamilyMembers.data ?: emptyList(),
                    initLoadingState = LoadingState.SuccessfulLoad
                )
            } else if (resourceFamilyMembers is Resource.Loading ||
                resourceUser is Resource.Loading
            ) {
                _state.value = state.value.copy(
                    initLoadingState = LoadingState.Loading
                )
            } else {
                _state.value = state.value.copy(
                    initLoadingState = LoadingState.FailedLoad
                )
            }
        }
    }

    private fun openBottomSheet(isOpen: Boolean) {
        _state.value = state.value.copy(isOpenBottomSheet = isOpen)
    }

    private fun getFamily() {
        _state.value = state.value.copy(loadingState = LoadingState.Loading)
        viewModelScope.launch {
            when (val resource = getFamilyMembers()) {
                is Resource.Error -> {
                    _state.value = state.value.copy(loadingState = LoadingState.FailedLoad)
                }

                is Resource.Loading -> {
                    _state.value = state.value.copy(loadingState = LoadingState.Loading)
                }

                is Resource.Success -> {
                    _state.value = state.value.copy(
                        familyList = resource.data ?: emptyList(),
                        loadingState = LoadingState.SuccessfulLoad
                    )
                }
            }
        }
    }

    private fun addMemberFamily(
        addUserId: String
    ) {
        _state.value = state.value.copy(loadingState = LoadingState.Loading)
        viewModelScope.launch {

            val hasIdInList = state.value.familyList.any { familyMember ->
                familyMember.id == addUserId
            }

            if (!hasIdInList) {

                when (val resource = attachUser(state.value.user?.id ?: "", addUserId)) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            loadingState = LoadingState.FailedLoad,
                            isOpenErrorDialog = true,
                            errorText = resource.message
                        )
                    }

                    is Resource.Loading -> {
                        _state.value = state.value.copy(loadingState = LoadingState.Loading)
                    }

                    is Resource.Success -> {
                        getFamily()
                    }
                }
            } else _state.value = state.value.copy(
                loadingState = LoadingState.FailedLoad,
                isOpenErrorDialog = true,
                errorText = "Пользователь уже добавлен"
            )
        }
    }

    private fun deleteMemberFamily(
        deleteUserId: String
    ) {
        _state.value = state.value.copy(loadingState = LoadingState.Loading)
        viewModelScope.launch {
            when (val resource = detachUser(state.value.user?.id ?: "", deleteUserId)) {
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        loadingState = LoadingState.FailedLoad,
                        isOpenErrorDialog = true,
                        errorText = resource.message
                    )
                }

                is Resource.Loading -> {
                    _state.value = state.value.copy(loadingState = LoadingState.Loading)
                }

                is Resource.Success -> {
                    getFamily()
                }
            }
        }
    }

    private fun addUserPoint(toUserId: String, point: Long) {

        _state.value = state.value.copy(loadingState = LoadingState.Loading)

        viewModelScope.launch {
            when (val resource = sendPoints(toUserId, point)) {
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        loadingState = LoadingState.FailedLoad,
                        isOpenErrorDialog = true,
                        errorText = resource.message
                    )
                }

                is Resource.Loading -> {
                    _state.value = state.value.copy(loadingState = LoadingState.Loading)
                }

                is Resource.Success -> {
                    getFamily()
                }

            }
        }
    }

    private fun getUserPoint(fromUserId: String, point: Long) {

        _state.value = state.value.copy(loadingState = LoadingState.Loading)

        viewModelScope.launch {
            when (val resource = getPoints(fromUserId, point)) {
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        loadingState = LoadingState.FailedLoad,
                        isOpenErrorDialog = true,
                        errorText = resource.message
                    )
                }

                is Resource.Loading -> {
                    _state.value = state.value.copy(loadingState = LoadingState.Loading)
                }

                is Resource.Success -> {
                    getFamily()
                }
            }
        }
    }
}