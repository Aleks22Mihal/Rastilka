package com.rastilka.presentation.screens.login_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastilka.common.app_data.LoadingState
import com.rastilka.common.app_data.LogInBody
import com.rastilka.common.app_data.LoggingStatus
import com.rastilka.data.data_source.Internal_storage.DataPreferences
import com.rastilka.data.data_source.remote.ApiService
import com.rastilka.domain.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: ApiService,
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
        Log.e("session Key: ", dataPreferences.session.toString())
        getUserBySession()
    }

    private fun getUserBySession() {
        _loadingStatusInit.value = LoadingState.Loading
        viewModelScope.launch {
            try {
                if (dataPreferences.session.first().isNotEmpty()) {
                    val response = repository.getUserBySession()
                    if (response.isSuccessful) {
                        _user.value = response.body()
                        _loadingStatusInit.value = LoadingState.SuccessfulLoad
                    } else {
                        _loadingStatusInit.value = LoadingState.FailedLoad
                    }
                } else {
                    _loadingStatusInit.value = LoadingState.SuccessfulLoad
                }
            } catch (e: Exception) {
                _loadingStatusInit.value = LoadingState.FailedLoad
                e.printStackTrace()
            }
        }
    }

    fun logIn(email: String, password: String) {
        _loadingStatus.value = LoadingState.Loading
        Log.e("2 ", "1")
        userStatusNull()
        viewModelScope.launch {
            Log.e("2 ", "2")
            try {

                val response = repository.login(LogInBody(mail = email, password = password))
                Log.e("2 ", "3")
                if (response.isSuccessful) {
                    Log.e("2 ", "4")
                    dataPreferences.saveSession(response.body()?.session!!)
                    _user.value = response.body()?.user
                    _userStatus.value = response.body()?.loginCondition
                    Log.e("2 ", "5")
                    _loadingStatus.value = LoadingState.SuccessfulLoad

                } else {
                    Log.e("2 ", "6")
                    _loadingStatus.value = LoadingState.FailedLoad
                }
            } catch (e: Exception) {
                Log.e("2 ", "${e.printStackTrace()}")
                _loadingStatus.value = LoadingState.FailedLoad
            }
        }
    }

    fun logOut() {
        _loadingStatusInit.value = LoadingState.Loading
        viewModelScope.launch {
            try {
                val repositoryLogOut = repository.logout()
                if (repositoryLogOut.isSuccessful) { getUserBySession() }
            } catch (e: Exception) {
                _loadingStatusInit.value = LoadingState.SuccessfulLoad
            }
        }
    }

    fun userStatusNull() {
        _userStatus.value = null
    }
}