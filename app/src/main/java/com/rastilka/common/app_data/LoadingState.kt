package com.rastilka.common.app_data

sealed class LoadingState {
    data object Loading : LoadingState()
    data object SuccessfulLoad : LoadingState()
    data object FailedLoad : LoadingState()
}