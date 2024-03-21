package com.rastilka.domain.use_case

import com.rastilka.common.Resource
import com.rastilka.common.app_data.TypeIdForApi
import com.rastilka.domain.models.TaskOrWish
import com.rastilka.domain.repository.MainRepository
import javax.inject.Inject

class GetTasksOrWishesUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    suspend operator fun invoke(type: TypeIdForApi): Resource<List<TaskOrWish>> =
        mainRepository.getTaskOrWish(type)
}