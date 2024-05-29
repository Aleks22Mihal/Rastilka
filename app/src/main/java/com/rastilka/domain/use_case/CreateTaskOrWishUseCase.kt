package com.rastilka.domain.use_case

import android.net.Uri
import com.rastilka.domain.repository.MainRepository
import javax.inject.Inject

class CreateTaskOrWishUseCase @Inject constructor(private val mainRepository: MainRepository) {
    suspend operator fun invoke(
        typeId: String,
        lastUrl: String,
        h1: String,
        price: String? = null,
        picture: Uri?,
        date: String? = null,
        forUserId: String? = null
    ) = mainRepository.createTaskOrWish(typeId, lastUrl, h1, price, picture, date, forUserId)
}