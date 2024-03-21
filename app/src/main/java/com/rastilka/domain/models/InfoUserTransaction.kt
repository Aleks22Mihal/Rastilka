package com.rastilka.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class InfoUserTransaction(
    @Json(name = "phone")
    val phone: String? = null,
    @Json(name = "transactionAccount")
    val transactionAccount: String? = null,
    @Json(name = "surname")
    val surname: String? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "mail")
    val mail: String? = null,
    @Json(name = "picture")
    val picture: String? = null,
)