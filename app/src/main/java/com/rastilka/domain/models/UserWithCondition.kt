package com.rastilka.domain.models

import com.rastilka.common.app_data.LoggingStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class UserWithCondition(
    @Json(name = "loginCondition")
    val loginCondition: LoggingStatus,
    @Json(name = "session")
    val session: String? = null,
    @Json(name = "user")
    val user: User? = null
)