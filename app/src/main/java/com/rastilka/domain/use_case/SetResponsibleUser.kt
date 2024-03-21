package com.rastilka.domain.use_case

import com.rastilka.domain.repository.MainRepository
import javax.inject.Inject

class SetResponsibleUser @Inject constructor(
    private val repository: MainRepository
){
    suspend operator fun invoke(productUrl: String, userId: String) =
        repository.setResponsibleUser(productUrl, userId)

}