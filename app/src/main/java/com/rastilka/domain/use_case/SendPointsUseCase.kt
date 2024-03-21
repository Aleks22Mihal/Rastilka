package com.rastilka.domain.use_case

import com.rastilka.domain.repository.MainRepository
import javax.inject.Inject

class SendPointsUseCase @Inject constructor(private val repository: MainRepository) {
    suspend operator fun invoke(toUserId: String, points: Int, comment: String? = null) =
        repository.sendPoint(toUserId, points, comment)
}