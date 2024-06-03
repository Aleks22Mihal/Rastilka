package com.rastilka.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class TaskOrWishFloatDTO(
    @Json(name = "price")
    val price: Float? = null,
    @Json(name = "salePrice")
    val salePrice: Float? = null,
)
