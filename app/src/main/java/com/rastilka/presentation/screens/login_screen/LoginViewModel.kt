package com.rastilka.presentation.screens.login_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastilka.common.Resource
import com.rastilka.common.app_data.LoadingState
import com.rastilka.common.app_data.LogInBody
import com.rastilka.common.app_data.LoggingStatus
import com.rastilka.data.data_source.Internal_storage.DataPreferences
import com.rastilka.data.data_source.remote.ApiService
import com.rastilka.domain.models.User
import com.rastilka.domain.use_case.GetUserBySessionKeyUseCase
import com.rastilka.domain.use_case.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: ApiService,
    private val getUserBySessionKey: GetUserBySessionKeyUseCase,
    private val login: LoginUseCase,
    private val dataPreferences: DataPreferences
) : ViewModel() {

    private var _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private var _userStatus = MutableStateFlow<LoggingStatus?>(null)
    val userStatus = _userStatus.asStateFlow()

    private var _loadingStatus = MutableStateFlow<LoadingState>(LoadingState.SuccessfulLoad)
    val loadingState = _loadingStatus.asStateFlow()

    private var _loadingStatusInit = MutableStateFlow<LoadingState>(LoadingState.SuccessfulLoad)
    val loadingStatusInit = _loadingStatusInit.asStateFlow()

    init {
        init()
    }

    fun init() {
        getUserBySession()
    }

    private fun getUserBySession() {
        _loadingStatusInit.value = LoadingState.Loading
        viewModelScope.launch {

            if (dataPreferences.session.first().isNotEmpty()) {
                when (val resource = getUserBySessionKey()) {
                    is Resource.Error -> {
                        _loadingStatusInit.value = LoadingState.FailedLoad
                    }

                    is Resource.Loading -> {
                        _loadingStatusInit.value = LoadingState.Loading
                    }

                    is Resource.Success -> {
                        _user.value = resource.data
                        _loadingStatusInit.value = LoadingState.SuccessfulLoad
                    }
                }
            }else{
                _loadingStatusInit.value = LoadingState.SuccessfulLoad
            }
        }
    }

    fun logIn(email: String, password: String) {
        _loadingStatus.value = LoadingState.Loading
        userStatusNull()
        viewModelScope.launch {
            when(val resource = login(LogInBody(mail = email, password = password))){
                is Resource.Error ->{
                    _loadingStatus.value = LoadingState.FailedLoad
                }
                is Resource.Loading -> {
                    _loadingStatus.value = LoadingState.Loading
                }
                is Resource.Success -> {
                    dataPreferences.saveSession(resource.data?.session!!)
                    _user.value = resource.data.user
                    _userStatus.value = resource.data.loginCondition
                    _loadingStatus.value = LoadingState.SuccessfulLoad
                }
            }
        }
    }

    fun logOut() {
        _loadingStatusInit.value = LoadingState.Loading
        viewModelScope.launch {
            try {
                val repositoryLogOut = repository.logout()
                if (repositoryLogOut.isSuccessful) {
                    getUserBySession()
                }
            } catch (e: Exception) {
                _loadingStatusInit.value = LoadingState.SuccessfulLoad
            }
        }
    }

    fun userStatusNull() {
        _userStatus.value = null
    }
}