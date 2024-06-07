package com.rastilka.domain.use_case

import com.rastilka.common.Resource
import com.rastilka.domain.models.UserWithCondition
import com.rastilka.domain.repository.MainRepository
import javax.inject.Inject

class RegistrationUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): Resource<UserWithCondition> {
        return repository.registration(name, email, password)
    }
}
