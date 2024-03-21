package com.rastilka.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class Transaction(
    @Json(name = "status")
    val status: String? = null,
    @Json(name = "id")
    val id: String,
    @Json(name = "comment")
    val comment: String? = null,
    @Json(name = "transaction")
    val transaction: Long,
    @Json(name = "date")
    val date: String? = null,
    @Json(name = "recipName")
    val recipeName: String? = null,
    @Json(name = "authTransacion")
    val authTransaction: InfoUserTransaction? = null,
    @Json(name = "recipTransacionId")
    val recipeTransactionId: String? = null,
    @Json(name = "authTransactionId")
    val authTransactionId: String? = null,
    @Json(name = "transactionString")
    val transactionString: String? = null,
    @Json(name = "recipTransacion")
    val recipeTransaction: InfoUserTransaction? = null,
)
