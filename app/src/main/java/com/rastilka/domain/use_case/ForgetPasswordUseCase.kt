package com.rastilka.domain.use_case

import com.rastilka.common.Resource
import com.rastilka.domain.repository.MainRepository
import javax.inject.Inject

class ForgetPasswordUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(email: String): Resource<Unit> {
        return repository.forgetPassword(email)
    }
}