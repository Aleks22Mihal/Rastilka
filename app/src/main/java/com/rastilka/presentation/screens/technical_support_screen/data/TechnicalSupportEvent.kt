package com.rastilka.presentation.screens.technical_support_screen.data

sealed class TechnicalSupportEvent {

    data object Refresh : TechnicalSupportEvent()

    data object SendMessage: TechnicalSupportEvent()

    data object GetMessage: TechnicalSupportEvent()

    data class ChangeMessage(
        val message: String
    ): TechnicalSupportEvent()
}