package com.rastilka.domain.use_case

import com.rastilka.common.Resource
import com.rastilka.domain.repository.MainRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(private val repository: MainRepository) {
    suspend operator fun invoke(): Resource<Unit> {
        return repository.logout()
    }
}