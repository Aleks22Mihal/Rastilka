package com.rastilka.domain.use_case

import com.rastilka.common.Resource
import com.rastilka.domain.models.User
import com.rastilka.domain.repository.MainRepository
import javax.inject.Inject

class AttachUserUseCase @Inject constructor(private val mainRepository: MainRepository) {

    suspend operator fun invoke(
        userOneId: String,
        userTwoId: String,
    ):Resource<User> {
       return mainRepository.attachUser(userOneId, userTwoId)
    }
}