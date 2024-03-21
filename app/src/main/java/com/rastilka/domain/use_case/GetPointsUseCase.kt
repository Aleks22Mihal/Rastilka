package com.rastilka.domain.use_case

import com.rastilka.domain.repository.MainRepository
import javax.inject.Inject

class GetPointsUseCase @Inject constructor(private val repository: MainRepository) {
    suspend operator fun invoke(fromUserId: String, points: Int, comment: String? = null) =
        repository.getPoint(fromUserId, points, comment)
}