package com.rastilka.domain.use_case

import com.rastilka.domain.repository.MainRepository
import javax.inject.Inject

class GetTransactionUseCase @Inject constructor(private val mainRepository: MainRepository) {

    suspend operator fun invoke() = mainRepository.getTransaction()
}
