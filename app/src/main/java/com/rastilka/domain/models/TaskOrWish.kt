package com.rastilka.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class TaskOrWish(
    @Json(name = "value")
    val value: TaskOrWishValue,
    @Json(name = "array")
    val array: TaskOrWishArray,
    @Json(name = "uuid")
    val uuid: TaskOrWishUuid,
    @Json(name = "float")
    val float: TaskOrWishFloat,
)