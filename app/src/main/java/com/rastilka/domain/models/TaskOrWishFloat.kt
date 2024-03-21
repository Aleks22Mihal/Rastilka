package com.rastilka.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class TaskOrWishFloat(
    @Json(name = "price")
    val price: Float? = null,
    @Json(name = "salePrice")
    val salePrice: Float? = null,
    )