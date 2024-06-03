package com.rastilka.domain.use_case

import com.rastilka.common.Resource
import com.rastilka.common.app_data.TypeIdForApi
import com.rastilka.domain.repository.MainRepository
import javax.inject.Inject

class ChangeIndexUseCase @Inject constructor(private val repository: MainRepository) {
    suspend operator fun invoke(
        typeId: TypeIdForApi,
        urlFrom: String,
        urlTo: String
    ): Resource<Unit> {
        return repository.changeIndexProducts(typeId, urlFrom, urlTo)
    }
}
