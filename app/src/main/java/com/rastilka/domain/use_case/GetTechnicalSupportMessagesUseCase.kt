package com.rastilka.domain.use_case

import com.rastilka.common.Resource
import com.rastilka.domain.models.TechnicalSupportMessage
import com.rastilka.domain.repository.MainRepository
import javax.inject.Inject

class GetTechnicalSupportMessagesUseCase @Inject constructor(
    private val repository: MainRepository
) {
    suspend operator fun invoke(): Resource<List<TechnicalSupportMessage>> {
        return repository.getTickets()
    }
}