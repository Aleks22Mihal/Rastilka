package com.rastilka.domain.use_case

import com.rastilka.common.Resource
import com.rastilka.domain.models.TechnicalSupportMessage
import com.rastilka.domain.repository.MainRepository
import javax.inject.Inject

class SendTechnicalSupportMessageUseCase @Inject constructor(
    private val repository: MainRepository
) {
    suspend operator fun invoke(message: String): Resource<List<TechnicalSupportMessage>> {
        return repository.postTickets(message = message)
    }
}
