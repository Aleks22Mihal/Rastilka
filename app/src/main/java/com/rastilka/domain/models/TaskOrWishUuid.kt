package com.rastilka.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class TaskOrWishUuid(
    @Json(name = "didUsers")
    val didUsers: List<String> = emptyList(),
    @Json(name = "forUsers")
    val forUsers: List<String> = emptyList(),
)