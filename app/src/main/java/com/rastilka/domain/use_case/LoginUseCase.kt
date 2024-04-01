package com.rastilka.domain.use_case

import com.rastilka.common.Resource
import com.rastilka.common.app_data.LogInBody
import com.rastilka.domain.models.UserWithCondition
import com.rastilka.domain.repository.MainRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: MainRepository) {

     suspend operator fun invoke(logInBody: LogInBody): Resource<UserWithCondition> {
         return repository.login(logInBody)
     }
}