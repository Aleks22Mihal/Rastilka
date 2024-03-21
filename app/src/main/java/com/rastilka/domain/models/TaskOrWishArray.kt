package com.rastilka.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class TaskOrWishArray(
    @Json(name = "optionalImages")
    val optionalImages: List<String>? = null,
    @Json(name = "parent_id")
    val parentId: List<String>? = null,
)