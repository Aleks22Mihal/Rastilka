package com.rastilka.presentation.screens.family_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastilka.common.app_data.LoadingState
import com.rastilka.common.app_data.PriceBody
import com.rastilka.data.data_source.remote.ApiService
import com.rastilka.domain.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FamilyViewModel @Inject constructor(
    private val repository: ApiService
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
            try {
                val response = repository.getFamilyList()
                if (response.isSuccessful) {
                    _familyList.value = response.body()!!
                    _initLoadingState.value = LoadingState.SuccessfulLoad
                } else {
                    _initLoadingState.value = LoadingState.FailedLoad
                }
            } catch (e: Exception) {
                _initLoadingState.value = LoadingState.FailedLoad
                e.printStackTrace()
            }
        }
    }

    fun addMemberFamily(
        userOneId: String,
        userTwoId: String
    ) {
        _loadingState.value = LoadingState.Loading
        viewModelScope.launch {
            try {
                val response = repository.attachUser(userOneId, userTwoId)
                if (response.isSuccessful) {
                    val responseFamily = repository.getFamilyList()
                    if (response.isSuccessful) {
                        _familyList.value = responseFamily.body()!!
                        _loadingState.value = LoadingState.SuccessfulLoad
                    } else {
                        _loadingState.value = LoadingState.FailedLoad
                    }
                } else {
                    _loadingState.value = LoadingState.FailedLoad
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _loadingState.value = LoadingState.FailedLoad
            }
        }
    }

    fun deleteMemberFamily(
        userOneId: String,
        userTwoId: String
    ) {
        _loadingState.value = LoadingState.Loading
        viewModelScope.launch {
            try {
                val response = repository.detachUser(userOneId, userTwoId)
                if (response.isSuccessful) {
                    val responseFamily = repository.getFamilyList()
                    if (response.isSuccessful) {
                        _familyList.value = responseFamily.body()!!
                        _loadingState.value = LoadingState.SuccessfulLoad
                    } else {
                        _loadingState.value = LoadingState.FailedLoad
                    }
                } else {
                    _loadingState.value = LoadingState.FailedLoad
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _loadingState.value = LoadingState.FailedLoad
            }
        }
    }

    fun addUserPoint(toUserId: String, point: Int) {
        _loadingState.value = LoadingState.Loading
        viewModelScope.launch {
            try {
                val response = repository.sendPoint(toUserId, point, PriceBody())
                if (response.isSuccessful) {
                    val responseFamily = repository.getFamilyList()
                    if (response.isSuccessful) {
                        _familyList.value = responseFamily.body()!!
                        _loadingState.value = LoadingState.SuccessfulLoad
                    } else {
                        _loadingState.value = LoadingState.FailedLoad
                    }
                } else {
                    _loadingState.value = LoadingState.FailedLoad
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _loadingState.value = LoadingState.FailedLoad
            }
        }
    }

    fun getUserPoint(fromUserId: String, point: Int) {
        _loadingState.value = LoadingState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getPoint(fromUserId, point,PriceBody())
                if (response.isSuccessful) {
                    val responseFamily = repository.getFamilyList()
                    if (response.isSuccessful) {
                        _familyList.value = responseFamily.body()!!
                        _loadingState.value = LoadingState.SuccessfulLoad
                    } else {
                        _loadingState.value = LoadingState.FailedLoad
                    }
                } else {
                    _loadingState.value = LoadingState.FailedLoad
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _loadingState.value = LoadingState.FailedLoad
            }
        }
    }
}