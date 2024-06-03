package com.rastilka.domain.models

import com.rastilka.common.app_data.StatusUser


data class User(
    val cargoType: String? = null,
    val mail: String? = null,
    val needDelivery: Boolean? = null,
    val balance: Int? = null,
    val userType: StatusUser? = null,
    val comment: String? = null,
    val city: String? = null,
    val thisOrderIsntCreated: Boolean? = null,
    val wantEdit: Boolean? = null,
    val address: String? = null,
    val deliveryCost: Int? = null,
    val countPointsTotal: Int? = null,
    val lang: String? = null,
    val userExist: Boolean,
    val phone: String? = null,
    val createDate: String? = null,
    val country: String? = null,
    val picture: String? = null,
    val wantUsePoints: Boolean? = null,
    val surname: String? = null,
    val id: String = "",
    val name: String = "",
    val canUsePoints: Long = 0,
)