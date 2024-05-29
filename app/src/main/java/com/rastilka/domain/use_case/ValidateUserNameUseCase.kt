package com.rastilka.domain.use_case

import com.rastilka.common.Resource
import javax.inject.Inject

class ValidateUserNameUseCase @Inject constructor() {

    operator fun invoke(name: String): Resource<Unit> {

        val allowedArrayCharacters = ".' ".toCharArray()

        return when {

            name.isBlank() -> Resource.Error(
                message = "Поле имя пользователя не может быть пустым"
            )

            name.any { it.isDigit() } -> Resource.Error(
                message = "В поле имя не допускаются цифры"
            )

            name.first().isWhitespace() -> Resource.Error(
                message = "В поле имя не допускаются пробел в начале"
            )

            name.last().isWhitespace() -> Resource.Error(
                message = "В поле имя не допускаются пробел в конце"
            )

            !name.split(" ").all { it.isNotBlank() } -> Resource.Error(
                message = "В поле имя не допускаются два и более пробелов подряд"
            )

            !name.all { if (it in allowedArrayCharacters) true else it.isLetter()  } ->  Resource.Error(
                message = "В поле имя не допускаются символы"
            )

            else -> Resource.Success(data = null)

        }
    }
}
