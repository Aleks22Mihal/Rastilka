package com.rastilka.domain.models

data class Transaction(
    val status: String? = null,
    val id: String,
    val comment: String? = null,
    val transaction: Long,
    val date: String? = null,
    val recipeName: String? = null,
    val authTransaction: InfoUserTransaction? = null,
    val recipeTransactionId: String? = null,
    val authTransactionId: String? = null,
    val transactionString: String? = null,
    val recipeTransaction: InfoUserTransaction? = null,
)
