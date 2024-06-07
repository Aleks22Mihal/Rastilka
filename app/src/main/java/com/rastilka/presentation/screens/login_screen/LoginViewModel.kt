package com.rastilka.presentation.screens.login_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastilka.common.Resource
import com.rastilka.common.app_data.LoadingState
import com.rastilka.common.app_data.LoggingStatus
import com.rastilka.domain.use_case.ForgetPasswordUseCase
import com.rastilka.domain.use_case.GetSessionKeyUseCase
import com.rastilka.domain.use_case.GetUserBySessionKeyUseCase
import com.rastilka.domain.use_case.LogOutUseCase
import com.rastilka.domain.use_case.LoginUseCase
import com.rastilka.domain.use_case.RegistrationUseCase
import com.rastilka.domain.use_case.SaveSessionKeyUseCase
import com.rastilka.domain.use_case.ValidateEmailUseCase
import com.rastilka.domain.use_case.ValidatePasswordUseCase
import com.rastilka.domain.use_case.ValidateUserNameUseCase
import com.rastilka.presentation.screens.login_screen.data.LoginScreenEvent
import com.rastilka.presentation.screens.login_screen.data.LoginScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getUserBySessionKey: GetUserBySessionKeyUseCase,
    private val loginUseCase: LoginUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val saveSessionKeyUseCase: SaveSessionKeyUseCase,
    private val getSessionKeyUseCase: GetSessionKeyUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateUserNameUseCase: ValidateUserNameUseCase,
    private val registrationUseCase: RegistrationUseCase,
    private val forgetPasswordUseCase: ForgetPasswordUseCase
) : ViewModel() {

    private var _state = MutableStateFlow(LoginScreenState())
    val state = _state.asStateFlow()

    init {
        getUserBySession()
    }

    fun onEvent(event: LoginScreenEvent) {
        when (event) {

            LoginScreenEvent.Refresh -> {
                getUserBySession()
            }

            is LoginScreenEvent.EditEmail -> {
                _state.value = state.value.copy(
                    email = event.editEmail,
                    errorEmail = null
                )
            }

            is LoginScreenEvent.EditPassword -> {
                _state.value = state.value.copy(
                    password = event.editPassword,
                    errorPassword = null
                )
            }

            is LoginScreenEvent.VisiblePassword -> {
                _state.value = state.value.copy(
                    isVisiblePassword = event.isVisible
                )
            }

            LoginScreenEvent.LogIn -> {
                logIn()
            }

            LoginScreenEvent.LogOut -> {
                logOut()
            }

            is LoginScreenEvent.EditEmailRegistration -> {
                _state.value = state.value.copy(
                    emailRegistration = event.editEmail,
                    errorEmailRegistration = null
                )
            }

            is LoginScreenEvent.EditNameRegistration -> {
                _state.value = state.value.copy(
                    nameRegistration = event.editName,
                    errorNameRegistration = null
                )
            }

            is LoginScreenEvent.EditPasswordRegistration -> {
                _state.value = state.value.copy(
                    passwordRegistration = event.editPassword,
                    errorPasswordRegistration = null
                )
            }

            is LoginScreenEvent.VisiblePasswordRegistration -> {
                _state.value = state.value.copy(
                    isVisiblePasswordRegistration = event.isVisible
                )
            }

            LoginScreenEvent.Registration -> {
                registration()
            }

            is LoginScreenEvent.EditEmailForgetPassword -> {
                _state.value = state.value.copy(
                    emailForgetPassword = event.editEmail,
                    errorEmailForgetPassword = null
                )
            }

            is LoginScreenEvent.VisibleForgetPasswordDialog -> {
                _state.value = state.value.copy(
                    isVisibleForgetPasswordDialog = event.isVisible
                )
            }

            LoginScreenEvent.ConfirmationForgetPassword -> {
                confirmationForgetPassword()
            }
        }
    }


    private fun getUserBySession() {
        _state.value = state.value.copy(
            initLoadingState = LoadingState.Loading
        )
        viewModelScope.launch {

            if (getSessionKeyUseCase().isNotEmpty()) {
                when (val resource = getUserBySessionKey()) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            initLoadingState = LoadingState.FailedLoad
                        )
                    }

                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            initLoadingState = LoadingState.Loading
                        )
                    }

                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            user = resource.data,
                            initLoadingState = LoadingState.SuccessfulLoad
                        )
                    }
                }
            } else {
                _state.value = state.value.copy(
                    initLoadingState = LoadingState.SuccessfulLoad
                )
            }
        }
    }

    private fun logIn() {

        val validateEmailResource = validateEmailUseCase(state.value.email)
        val validatePasswordResource = validatePasswordUseCase(state.value.password)

        val hasError = listOf(
            validateEmailResource,
            validatePasswordResource,
        ).any { it is Resource.Error }

        if (hasError) {
            _state.value = state.value.copy(
                errorEmail = validateEmailResource.message,
                errorPassword = validatePasswordResource.message,
            )
        } else {
            _state.value = state.value.copy(
                loadingState = LoadingState.Loading,
                userStatus = null
            )
            viewModelScope.launch {
                when (val resource = loginUseCase(
                    email = state.value.email,
                    password = state.value.password
                )) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            loadingState = LoadingState.FailedLoad,
                        )
                    }

                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            loadingState = LoadingState.Loading,
                        )
                    }

                    is Resource.Success -> {

                        _state.value = state.value.copy(
                            user = resource.data?.user,
                            userStatus = resource.data?.loginCondition,
                            loadingState = LoadingState.SuccessfulLoad,
                        )

                        when (state.value.userStatus) {
                            LoggingStatus.mailIsntExist -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                    errorEmail = "Такой пользователь не зарегистрирован"
                                )
                            }

                            LoggingStatus.passwordError -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                    errorPassword = "Пароль неверный"
                                )
                            }

                            LoggingStatus.getParametersError -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                    errorEmail = "Неверные параметры",
                                    errorPassword = "Неверные параметры",
                                )
                            }

                            LoggingStatus.mailIsBusy -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                    errorEmail = "Почта занята"
                                )
                            }

                            LoggingStatus.mailForbidden -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                    errorEmail = "Неверный формат почты"
                                )
                            }

                            LoggingStatus.mailWasFromOldSite -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                    errorEmail = "Почта больше не активна"
                                )
                            }

                            null -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                )
                            }

                            else -> {
                                saveSessionKeyUseCase(resource.data?.session!!)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun registration() {

        val validateNameResource = validateUserNameUseCase(state.value.nameRegistration)
        val validateEmailResource = validateEmailUseCase(state.value.emailRegistration)
        val validatePasswordResource = validatePasswordUseCase(state.value.passwordRegistration)

        val hasError = listOf(
            validateEmailResource,
            validateEmailResource,
            validatePasswordResource,
        ).any { it is Resource.Error }

        if (hasError) {
            _state.value = state.value.copy(
                errorNameRegistration = validateNameResource.message,
                errorEmailRegistration = validateEmailResource.message,
                errorPasswordRegistration = validatePasswordResource.message,
            )
        } else {
            _state.value = state.value.copy(
                loadingState = LoadingState.Loading,
                userStatus = null
            )
            viewModelScope.launch {
                when (val resource =
                    registrationUseCase(
                        name = state.value.nameRegistration,
                        email = state.value.emailRegistration,
                        password = state.value.passwordRegistration
                    )
                ) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            loadingState = LoadingState.FailedLoad,
                        )
                    }

                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            loadingState = LoadingState.Loading,
                        )
                    }

                    is Resource.Success -> {

                        _state.value = state.value.copy(
                            user = resource.data?.user,
                            userStatus = resource.data?.loginCondition,
                            loadingState = LoadingState.SuccessfulLoad,
                        )

                        when (state.value.userStatus) {

                            LoggingStatus.passwordError -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                    errorPasswordRegistration = "Пароль неверный"
                                )
                            }

                            LoggingStatus.getParametersError -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                    errorEmailRegistration = "Неверные параметры",
                                    errorPasswordRegistration = "Неверные параметры",
                                )
                            }

                            LoggingStatus.mailIsBusy -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                    errorEmailRegistration = "Почта занята"
                                )
                            }

                            LoggingStatus.mailForbidden -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                    errorEmailRegistration = "Неверный формат почты"
                                )
                            }

                            LoggingStatus.mailWasFromOldSite -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                    errorEmailRegistration = "Почта больше не активна"
                                )
                            }

                            null -> {
                                _state.value = state.value.copy(
                                    loadingState = LoadingState.FailedLoad,
                                )
                            }

                            else -> {
                                _state.value = state.value.copy(
                                    email = state.value.emailRegistration,
                                    password = state.value.passwordRegistration,
                                    nameRegistration = "",
                                    emailRegistration = "",
                                    passwordRegistration = ""
                                )
                                saveSessionKeyUseCase(resource.data?.session!!)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun confirmationForgetPassword() {
        _state.value = state.value.copy(
            isSnackBarShowing = false,
            loadingStateForgetPassword = LoadingState.Loading
        )

        val validateEmailResource = validateEmailUseCase(state.value.emailForgetPassword)

        if (validateEmailResource is Resource.Error) {
            _state.value = state.value.copy(
                errorEmailForgetPassword = validateEmailResource.message,
                loadingStateForgetPassword = LoadingState.SuccessfulLoad
            )
        } else {

            viewModelScope.launch {
                when (val resource = forgetPasswordUseCase(state.value.emailForgetPassword)) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            errorEmailForgetPassword = resource.message,
                            loadingStateForgetPassword = LoadingState.FailedLoad
                        )
                    }

                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            loadingStateForgetPassword = LoadingState.Loading
                        )
                    }

                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            isVisibleForgetPasswordDialog = false,
                            isSnackBarShowing = true,
                            loadingStateForgetPassword = LoadingState.SuccessfulLoad
                        )
                    }
                }
            }
        }
    }

    private fun logOut() {
        _state.value = state.value.copy(
            initLoadingState = LoadingState.Loading,
        )
        viewModelScope.launch {
            when (logOutUseCase()) {
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        initLoadingState = LoadingState.FailedLoad,
                    )
                }

                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        initLoadingState = LoadingState.Loading,
                    )
                }

                is Resource.Success -> {
                    getUserBySession()
                }
            }
        }
    }


}
