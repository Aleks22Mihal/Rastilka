package com.rastilka.presentation.screens.transaction_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastilka.data.data_source.remote.ApiService
import com.rastilka.domain.models.Transaction
import com.rastilka.common.app_data.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: ApiService,
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
            try {
                val response = repository.getTransaction()
                if (response.isSuccessful) {
                    _transactionList.value = response.body()!!
                    _initLoadingState.value = LoadingState.SuccessfulLoad
                } else {
                    _initLoadingState.value = LoadingState.FailedLoad
                }

            } catch (e: Exception) {
                _initLoadingState.value = LoadingState.FailedLoad
            }
        }
    }
}