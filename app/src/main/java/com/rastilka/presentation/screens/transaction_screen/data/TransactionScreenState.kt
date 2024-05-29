package com.rastilka.presentation.screens.transaction_screen.data

import com.rastilka.common.app_data.LoadingState
import com.rastilka.domain.models.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TransactionScreenState(
    val transactionList: List<Transaction> = emptyList(),
    val initLoadingState: LoadingState = LoadingState.SuccessfulLoad
)