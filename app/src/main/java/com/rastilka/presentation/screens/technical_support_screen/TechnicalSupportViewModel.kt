package com.rastilka.presentation.screens.technical_support_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastilka.common.Resource
import com.rastilka.common.app_data.LoadingState
import com.rastilka.domain.use_case.GetTechnicalSupportMessagesUseCase
import com.rastilka.domain.use_case.GetUserBySessionKeyUseCase
import com.rastilka.domain.use_case.SendTechnicalSupportMessageUseCase
import com.rastilka.presentation.screens.technical_support_screen.data.TechnicalSupportEvent
import com.rastilka.presentation.screens.technical_support_screen.data.TechnicalSupportState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TechnicalSupportViewModel @Inject constructor(
    private val getTechnicalSupportMessages: GetTechnicalSupportMessagesUseCase,
    private val sendTechnicalSupportMessage: SendTechnicalSupportMessageUseCase,
    private val getUserBySessionKeyUseCase: GetUserBySessionKeyUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(TechnicalSupportState())
    val state = _state.asStateFlow()

    init {
        init()
    }

    fun onEvent(event: TechnicalSupportEvent) {
        when (event) {
            TechnicalSupportEvent.Refresh -> {
                init()
            }

            is TechnicalSupportEvent.ChangeMessage -> {
                _state.value = state.value.copy(
                    message = event.message
                )
            }

            TechnicalSupportEvent.SendMessage -> {
                sendMessage()
            }

            TechnicalSupportEvent.GetMessage -> {
                getMessage()
            }
        }
    }

    private fun getMessage() {
        viewModelScope.launch {
            when (val resource = getTechnicalSupportMessages()) {
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        loadingState = LoadingState.FailedLoad,
                        message = resource.message ?: ""
                    )
                }

                is Resource.Loading -> {}

                is Resource.Success -> {
                    _state.value = state.value.copy(
                        listMessage = resource.data?.reversed() ?: emptyList()
                    )
                }
            }
        }
    }

    private fun init() {

        _state.value = state.value.copy(
            initLoadingState = LoadingState.Loading
        )

        viewModelScope.launch {
            val resourceUser = getUserBySessionKeyUseCase()
            val resourceTechnicalSupportMessages = getTechnicalSupportMessages()

            if (resourceTechnicalSupportMessages is Resource.Success
                && resourceUser is Resource.Success
            ) {

                _state.value = state.value.copy(
                    user = resourceUser.data,
                    listMessage = resourceTechnicalSupportMessages.data?.reversed() ?: emptyList(),
                    initLoadingState = LoadingState.SuccessfulLoad
                )

            } else if (resourceTechnicalSupportMessages is Resource.Loading
                || resourceUser is Resource.Loading
            ) {

                _state.value = state.value.copy(
                    initLoadingState = LoadingState.Loading
                )

            } else {
                _state.value = state.value.copy(
                    initLoadingState = LoadingState.FailedLoad,
                    message = resourceTechnicalSupportMessages.message ?: resourceUser.message ?: ""
                )
            }
        }
    }

    private fun sendMessage() {
        if (state.value.message.isNotBlank()) {
            _state.value = state.value.copy(
                loadingState = LoadingState.Loading
            )

            viewModelScope.launch {
                delay(5000)
                when (val resource = sendTechnicalSupportMessage(
                    state.value.message
                )
                ) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            loadingState = LoadingState.FailedLoad,
                            message = resource.message ?: ""
                        )
                    }

                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            loadingState = LoadingState.Loading,
                        )
                    }

                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            loadingState = LoadingState.SuccessfulLoad,
                            message = "",
                            listMessage = resource.data?.reversed() ?: emptyList()
                        )
                    }
                }
            }
        }
    }
}