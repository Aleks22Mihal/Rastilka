package com.rastilka.domain.use_case

import com.rastilka.common.Resource
import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {

    operator fun invoke(password: String?): Resource<Unit> {

        return if (password != null) {
            when {

                password.isBlank() -> Resource.Error(
                    message = "Пароль не может быть пустым"
                )

                password.length < LENGTH_PASSWORD -> Resource.Error(
                    message = "Пароль должен состоять из $LENGTH_PASSWORD символов"
                )

                password.any { it.isWhitespace() } -> Resource.Error(
                    message = "В поле пароль не допускаются пробелы"
                )

                !password.all { it.isLetterOrDigit() } -> Resource.Error(
                    message = "В поле имя не допускаются символы"
                )

                !password.any { it.isDigit() && password.any { text -> text.isLetter() } } -> Resource.Error(
                    message = "Пароль должен содержать хотя бы одну букву и цифру"
                )

                else -> {
                    Resource.Success(data = null)
                }
            }
        } else {
            Resource.Success(data = null)
        }
    }

    companion object {
        const val LENGTH_PASSWORD = 8
    }
}
