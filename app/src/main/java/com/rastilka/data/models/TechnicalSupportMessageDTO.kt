package com.rastilka.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class TechnicalSupportMessageDTO(
    @Json(name = "message")
    val message: String,
    @Json(name = "time")
    val time: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "picture")
    val picture: String
)
