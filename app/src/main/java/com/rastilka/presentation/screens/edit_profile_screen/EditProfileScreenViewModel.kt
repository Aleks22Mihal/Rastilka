package com.rastilka.presentation.screens.edit_profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastilka.common.Resource
import com.rastilka.common.app_data.LoadingState
import com.rastilka.domain.use_case.EditUserAndPasswordUseCase
import com.rastilka.domain.use_case.GetUserBySessionKeyUseCase
import com.rastilka.domain.use_case.ValidateEmailUseCase
import com.rastilka.domain.use_case.ValidatePasswordUseCase
import com.rastilka.domain.use_case.ValidateUserNameUseCase
import com.rastilka.presentation.screens.edit_profile_screen.data.EditProfileScreenEvent
import com.rastilka.presentation.screens.edit_profile_screen.data.EditProfileScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileScreenViewModel @Inject constructor(
    private val getUserUseCase: GetUserBySessionKeyUseCase,
    private val editUserAndPasswordUseCase: EditUserAndPasswordUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateUserNameUseCase: ValidateUserNameUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileScreenState())
    val state = _state.asStateFlow()

    init {
        init()
    }

    fun onEvent(event: EditProfileScreenEvent) {
        when (event) {
            EditProfileScreenEvent.Refresh -> {
                init()
            }

            is EditProfileScreenEvent.EditPhoto -> {
                _state.value = state.value.copy(
                    editPhoto = event.pathPhoto
                )
            }

            is EditProfileScreenEvent.EditName -> {
                _state.value = state.value.copy(
                    editName = event.editName,
                    editNameError = null
                )
            }

            is EditProfileScreenEvent.EditEmail -> {
                _state.value = state.value.copy(
                    editEmail = event.editEmail,
                    editEmailError = null
                )
            }

            is EditProfileScreenEvent.EditPassword -> {
                _state.value = state.value.copy(
                    editPassword = if (event.editPassword.isNullOrBlank()) null else event.editPassword,
                    editPasswordError = null
                )
            }

            EditProfileScreenEvent.EditUserAndPassword -> {
                editUserAndPassword()
            }
        }
    }

    private fun init() {
        _state.value = state.value.copy(
            initLoadingState = LoadingState.Loading
        )
        viewModelScope.launch {
            when (val resource = getUserUseCase()) {
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        user = resource.data,
                        editName = resource.data?.name ?: "",
                        editEmail = resource.data?.mail ?: "",
                        editPassword = null,
                        loadingState = LoadingState.SuccessfulLoad,
                        initLoadingState = LoadingState.SuccessfulLoad
                    )
                }

                is Resource.Error -> {
                    _state.value = state.value.copy(
                        initLoadingState = LoadingState.FailedLoad,
                        screenErrorMessage = resource.message ?: ""
                    )
                }

                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        initLoadingState = LoadingState.Loading
                    )
                }
            }
        }
    }

    private fun editUserAndPassword() {
        if (state.value.editName != state.value.user?.name ||
            state.value.editEmail != state.value.user?.mail ||
            state.value.editPassword != null ||
            state.value.editPhoto != null
        ) {
            val validateEmailResource = validateEmailUseCase(state.value.editEmail)
            val validatePasswordResource = validatePasswordUseCase(state.value.editPassword)
            val validateUserNameResource = validateUserNameUseCase(state.value.editName)

            val hasError = listOf(
                validateEmailResource,
                validatePasswordResource,
                validateUserNameResource,
            ).any { it is Resource.Error }

            if (hasError) {
                _state.value = state.value.copy(
                    editEmailError = validateEmailResource.message,
                    editPasswordError = validatePasswordResource.message,
                    editNameError = validateUserNameResource.message
                )
            } else {
                _state.value = state.value.copy(
                    loadingState = LoadingState.Loading
                )
                viewModelScope.launch {
                    when (
                        val resource = editUserAndPasswordUseCase(
                            name = state.value.editName,
                            email = state.value.editEmail,
                            password = state.value.editPassword,
                            pictureUri = state.value.editPhoto
                        )
                    ) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                user = resource.data,
                                editName = resource.data?.name ?: "",
                                editEmail = resource.data?.mail ?: "",
                                editPhoto = null,
                                editPassword = null,
                                loadingState = LoadingState.SuccessfulLoad
                            )
                        }

                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                initLoadingState = LoadingState.FailedLoad,
                                screenErrorMessage = resource.message ?: ""
                            )
                        }

                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                loadingState = LoadingState.Loading
                            )
                        }
                    }
                }
            }
        }
    }
}
