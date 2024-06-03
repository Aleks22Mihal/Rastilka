package com.rastilka.domain.use_case

import com.rastilka.common.Resource
import com.rastilka.domain.models.TaskOrWish
import com.rastilka.domain.repository.MainRepository
import javax.inject.Inject

class GetTaskUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    suspend operator fun invoke(productUrl: String): Resource<TaskOrWish> =
        mainRepository.getTaskOrWish(productUrl)
}
