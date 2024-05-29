package com.rastilka.domain.use_case

import com.rastilka.domain.repository.MainRepository
import javax.inject.Inject

class EditUserAndPasswordUseCase @Inject constructor(private val mainRepository: MainRepository) {

    suspend operator fun invoke(name: String, email: String, password: String?, pictureUri: String?) =
        mainRepository.editUserAndPassword(
            name = name,
            email = email,
            password = password,
            pictureUri = pictureUri
        )
}