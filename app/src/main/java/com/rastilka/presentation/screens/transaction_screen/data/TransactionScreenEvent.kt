package com.rastilka.presentation.screens.transaction_screen.data

sealed class TransactionScreenEvent {
    data object Refresh : TransactionScreenEvent()
}