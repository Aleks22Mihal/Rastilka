package com.rastilka.domain.models

import com.rastilka.common.app_data.LoggingStatus
import com.squareup.moshi.JsonClass

data class UserWithCondition(
    val loginCondition: LoggingStatus,
    val session: String? = null,
    val user: User? = null
)