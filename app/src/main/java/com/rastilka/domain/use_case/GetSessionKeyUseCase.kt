package com.rastilka.domain.use_case

import com.rastilka.data.data_source.Internal_storage.DataPreferences
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetSessionKeyUseCase @Inject constructor(private val dataPreferences: DataPreferences) {

    suspend operator fun invoke(): String {
        return dataPreferences.session.first()
    }

}
