package com.rastilka.domain.use_case

import com.rastilka.domain.repository.MainRepository
import javax.inject.Inject

class EditTaskOrWishUseCase @Inject constructor(private val mainRepository: MainRepository) {
    suspend operator fun invoke(productUrl: String, property: String, value: String) =
        mainRepository.editTask(productUrl, property, value)
}
