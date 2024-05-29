package com.rastilka.domain.use_case

import com.rastilka.common.Resource
import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {

    operator fun invoke(password: String?): Resource<Unit> {

        if (password != null) {

            if (password.length < LENGTH_PASSWORD) {
                return Resource.Error(
                    message = "Пароль должен состоять из $LENGTH_PASSWORD символов"
                )
            }

            val containsLetterAndDigits =
                password.any { it.isDigit() } && password.any { it.isLetter() }

            if (!containsLetterAndDigits) {

                return Resource.Error(
                    message = "Пароль должен содержать хотя бы одну букву и цифру"
                )
            }
            return Resource.Success(data = null)

        } else {

            return Resource.Success(data = null)

        }
    }

    companion object {
        const val LENGTH_PASSWORD = 8
    }

}