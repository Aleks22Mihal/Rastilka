package com.rastilka.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class TaskOrWishArrayDTO(
    @Json(name = "optionalImages")
    val optionalImages: List<String>? = null,
    @Json(name = "parent_id")
    val parentId: List<String>? = null,
)
