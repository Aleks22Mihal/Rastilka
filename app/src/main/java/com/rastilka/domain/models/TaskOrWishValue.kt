package com.rastilka.domain.models

import com.rastilka.common.app_data.TypeId
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class TaskOrWishValue(
    @Json(name = "parent_id")
    val parentId: String,
    @Json(name = "is_active")
    val isActive: String? = null,
    @Json(name = "url")
    val url: String,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "photo")
    val photo: String? = null,
    @Json(name = "h1")
    val h1: String,
    @Json(name = "rowId")
    val rowId: String? = null,
    @Json(name = "type_id")
    val typeId: TypeId? = null,
    @Json(name = "price")
    val price: String? = null,
    @Json(name = "salePrice")
    val salePrice: String? = null,
    @Json(name = "id")
    val id: String,
    @Json(name = "date")
    val date: String? = null,
    @Json(name = "assembly")
    val assembly: String? = null,
)