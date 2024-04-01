package com.rastilka.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class TaskOrWishDTO(
    @Json(name = "value")
    val value: TaskOrWishValueDTO,
    @Json(name = "array")
    val array: TaskOrWishArrayDTO,
    @Json(name = "uuid")
    val uuid: TaskOrWishUuidDTO,
    @Json(name = "float")
    val float: TaskOrWishFloatDTO,
)