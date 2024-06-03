package com.rastilka.domain.use_case

import com.rastilka.domain.repository.MainRepository
import javax.inject.Inject

class DeleteTaskOrWishUseCase @Inject constructor(private val mainRepository: MainRepository) {
    suspend operator fun invoke(productUrl: String) =
        mainRepository.deleteTaskOrWish(productUrl)
}
