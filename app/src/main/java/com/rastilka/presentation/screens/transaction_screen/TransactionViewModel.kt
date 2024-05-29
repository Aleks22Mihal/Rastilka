package com.rastilka.presentation.screens.transaction_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastilka.common.Resource
import com.rastilka.common.app_data.LoadingState
import com.rastilka.domain.models.Transaction
import com.rastilka.domain.use_case.GetTransactionUseCase
import com.rastilka.presentation.screens.family_screen.data.FamilyScreenEvent
import com.rastilka.presentation.screens.transaction_screen.data.TransactionScreenEvent
import com.rastilka.presentation.screens.transaction_screen.data.TransactionScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val getTransaction: GetTransactionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionScreenState())
    val state = _state.asStateFlow()

    init {
        init()
    }

    fun onEvent(event: TransactionScreenEvent){
        when(event){
            TransactionScreenEvent.Refresh -> {
                init()
            }
        }
    }

    private fun init() {
        _state.value = state.value.copy(initLoadingState = LoadingState.Loading)
        viewModelScope.launch {
            when (val resource = getTransaction()) {
                is Resource.Error -> {
                    _state.value = state.value.copy(initLoadingState = LoadingState.FailedLoad)
                }

                is Resource.Loading -> {
                    _state.value = state.value.copy(initLoadingState = LoadingState.Loading)
                }

                is Resource.Success -> {
                    _state.value = state.value.copy(
                        transactionList = resource.data ?: emptyList(),
                        initLoadingState = LoadingState.SuccessfulLoad
                    )
                }
            }
        }
    }
}