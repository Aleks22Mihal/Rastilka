package com.rastilka.domain.use_case

import com.rastilka.data.data_source.Internal_storage.DataPreferences
import javax.inject.Inject

class SaveSessionKeyUseCase @Inject constructor(private val dataPreferences: DataPreferences) {

    suspend operator fun invoke(sessionKey: String) {
        dataPreferences.saveSession(sessionKey)
    }

}
