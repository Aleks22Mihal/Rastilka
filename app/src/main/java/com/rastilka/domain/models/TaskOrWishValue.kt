package com.rastilka.domain.models

import com.rastilka.common.app_data.TypeId

data class TaskOrWishValue(
    val parentId: String,
    val isActive: String? = null,
    val url: String,
    val description: String? = null,
    val photo: String? = null,
    val h1: String,
    val rowId: String? = null,
    val typeId: TypeId? = null,
    val price: String? = null,
    val salePrice: String? = null,
    val id: String,
    val date: String? = null,
    val assembly: String? = null,
)
