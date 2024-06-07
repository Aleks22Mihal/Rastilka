package com.rastilka.domain.use_case

import com.rastilka.common.Resource
import java.util.regex.Pattern
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor() {

    operator fun invoke(email: String): Resource<Unit> {
        if (email.isBlank()) {
            return Resource.Error(
                message = "Почта не может быть пустой"
            )
        }

        val emailAddressPattern =
            Pattern.compile(
                buildString {
                    append("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]")
                    append("{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+")
                }
            )

        if (!emailAddressPattern.matcher(email).matches()) {
            return Resource.Error(
                message = "Неверный формат почты"
            )
        }
        return Resource.Success(data = null)
    }
}
