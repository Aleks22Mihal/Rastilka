package com.rastilka.presentation.screens.transaction_screen.data

import com.rastilka.common.app_data.LoadingState
import com.rastilka.domain.models.Transaction

data class TransactionScreenState(
    val transactionList: List<Transaction> = emptyList(),
    val initLoadingState: LoadingState = LoadingState.SuccessfulLoad
)
