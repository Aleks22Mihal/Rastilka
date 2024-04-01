package com.rastilka.presentation.screens.transaction_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastilka.common.Resource
import com.rastilka.common.app_data.LoadingState
import com.rastilka.domain.models.Transaction
import com.rastilka.domain.use_case.GetTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val getTransaction: GetTransactionUseCase
) : ViewModel() {

    private val _transactionList = MutableStateFlow<List<Transaction>>(emptyList())
    val transactionList = _transactionList.asStateFlow()

    private var _initLoadingState = MutableStateFlow<LoadingState>(LoadingState.SuccessfulLoad)
    val initLoadingState = _initLoadingState.asStateFlow()

    init {
        init()
    }

    fun init() {
        _initLoadingState.value = LoadingState.Loading
        viewModelScope.launch {
            when(val resource = getTransaction()){
                is Resource.Error -> {
                    _initLoadingState.value = LoadingState.FailedLoad
                }
                is Resource.Loading -> {
                    _initLoadingState.value = LoadingState.Loading
                }
                is Resource.Success -> {
                    _transactionList.value = resource.data ?: emptyList()
                    _initLoadingState.value = LoadingState.SuccessfulLoad
                }
            }
        }
    }
}