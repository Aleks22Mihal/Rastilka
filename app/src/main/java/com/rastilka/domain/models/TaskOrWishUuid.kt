package com.rastilka.domain.models

data class TaskOrWishUuid(
    val didUsers: List<String> = emptyList(),
    val forUsers: List<String> = emptyList(),
)