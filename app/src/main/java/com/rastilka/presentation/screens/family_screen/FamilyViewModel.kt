package com.rastilka.presentation.screens.family_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastilka.common.Resource
import com.rastilka.common.app_data.LoadingState
import com.rastilka.domain.models.User
import com.rastilka.domain.use_case.AttachUserUseCase
import com.rastilka.domain.use_case.DetachUserUseCase
import com.rastilka.domain.use_case.GetFamilyMembersUseCase
import com.rastilka.domain.use_case.GetPointsUseCase
import com.rastilka.domain.use_case.SendPointsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FamilyViewModel @Inject constructor(
    private val getFamilyMembers: GetFamilyMembersUseCase,
    private val attachUser: AttachUserUseCase,
    private val detachUser: DetachUserUseCase,
    private val getPoints: GetPointsUseCase,
    private val sendPoints: SendPointsUseCase
) : ViewModel() {

    private val _familyList = MutableStateFlow<List<User>>(emptyList())
    val familyList = _familyList.asStateFlow()

    private var _initLoadingState = MutableStateFlow<LoadingState>(LoadingState.SuccessfulLoad)
    val initLoadingState = _initLoadingState.asStateFlow()

    private var _loadingState = MutableStateFlow<LoadingState>(LoadingState.SuccessfulLoad)
    val loadingState = _loadingState.asStateFlow()

    init {
        init()
    }

    fun init() {
        getFamily()
    }

    private fun getFamily() {
        _initLoadingState.value = LoadingState.Loading
        viewModelScope.launch {
            when (val resource = getFamilyMembers()) {
                is Resource.Error -> {
                    _initLoadingState.value = LoadingState.FailedLoad
                }

                is Resource.Loading -> {
                    _initLoadingState.value = LoadingState.Loading
                }

                is Resource.Success -> {
                    _familyList.value = resource.data ?: emptyList()
                    _initLoadingState.value = LoadingState.SuccessfulLoad
                }
            }
        }
    }

    fun addMemberFamily(
        userOneId: String,
        userTwoId: String
    ) {
        _loadingState.value = LoadingState.Loading
        viewModelScope.launch {
            when (attachUser(userOneId, userTwoId)) {
                is Resource.Error -> {
                    _loadingState.value = LoadingState.FailedLoad
                }

                is Resource.Loading -> {
                    _loadingState.value = LoadingState.Loading
                }

                is Resource.Success -> {
                    when (val resourceFamilyMembers = getFamilyMembers()) {
                        is Resource.Error -> {
                            _loadingState.value = LoadingState.FailedLoad
                        }

                        is Resource.Loading -> {
                            _loadingState.value = LoadingState.Loading
                        }

                        is Resource.Success -> {
                            _familyList.value = resourceFamilyMembers.data ?: emptyList()
                        }
                    }
                }
            }
        }
    }

    fun deleteMemberFamily(
        userOneId: String,
        userTwoId: String
    ) {
        _loadingState.value = LoadingState.Loading
        viewModelScope.launch {
            when (detachUser(userOneId, userTwoId)) {
                is Resource.Error -> {
                    _loadingState.value = LoadingState.FailedLoad
                }

                is Resource.Loading -> {
                    _loadingState.value = LoadingState.Loading
                }

                is Resource.Success -> {
                    when (val resourceFamilyMembers = getFamilyMembers()) {
                        is Resource.Error -> {
                            _loadingState.value = LoadingState.FailedLoad
                        }

                        is Resource.Loading -> {
                            _loadingState.value = LoadingState.Loading
                        }

                        is Resource.Success -> {
                            _familyList.value = resourceFamilyMembers.data ?: emptyList()
                        }
                    }
                }
            }
        }
    }

    fun addUserPoint(toUserId: String, point: Int) {
        _loadingState.value = LoadingState.Loading
        viewModelScope.launch {
            when (sendPoints(toUserId, point)) {
                is Resource.Error -> {
                    _loadingState.value = LoadingState.FailedLoad
                }

                is Resource.Loading -> {
                    _loadingState.value = LoadingState.Loading
                }

                is Resource.Success -> {
                    when (val resourceFamilyMembers = getFamilyMembers()) {
                        is Resource.Error -> {
                            _loadingState.value = LoadingState.FailedLoad
                        }

                        is Resource.Loading -> {
                            _loadingState.value = LoadingState.Loading
                        }

                        is Resource.Success -> {
                            _familyList.value = resourceFamilyMembers.data ?: emptyList()
                        }
                    }
                }
            }
        }
    }

    fun getUserPoint(fromUserId: String, point: Int) {
        _loadingState.value = LoadingState.Loading
        viewModelScope.launch {
            when (getPoints(fromUserId, point)) {
                is Resource.Error -> {
                    _loadingState.value = LoadingState.FailedLoad
                }

                is Resource.Loading -> {
                    _loadingState.value = LoadingState.Loading
                }

                is Resource.Success -> {
                    when (val resourceFamilyMembers = getFamilyMembers()) {
                        is Resource.Error -> {
                            _loadingState.value = LoadingState.FailedLoad
                        }

                        is Resource.Loading -> {
                            _loadingState.value = LoadingState.Loading
                        }

                        is Resource.Success -> {
                            _familyList.value = resourceFamilyMembers.data ?: emptyList()
                        }
                    }
                }
            }
        }
    }
}