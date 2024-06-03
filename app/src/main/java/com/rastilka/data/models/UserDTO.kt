package com.rastilka.data.models

import com.rastilka.common.app_data.StatusUser
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = false)
data class UserDTO(
    @Json(name = "cargoType")
    val cargoType: String? = null,
    @Json(name = "mail")
    val mail: String? = null,
    @Json(name = "needDelivery")
    val needDelivery: Boolean? = null,
    @Json(name = "balance")
    val balance: Int? = null,
    @Json(name = "userType")
    val userType: StatusUser? = null,
    @Json(name = "comment")
    val comment: String? = null,
    @Json(name = "city")
    val city: String? = null,
    @Json(name = "thisOrderIsntCreated")
    val thisOrderIsntCreated: Boolean? = null,
    @Json(name = "wantEdit")
    val wantEdit: Boolean? = null,
    @Json(name = "address")
    val address: String? = null,
    @Json(name = "deliveryCost")
    val deliveryCost: Int? = null,
    @Json(name = "counPointsTotal")
    val countPointsTotal: Int? = null,
    @Json(name = "lang")
    val lang: String? = null,
    @Json(name = "userExist")
    val userExist: Boolean,
    @Json(name = "phone")
    val phone: String? = null,
    @Json(name = "createDate")
    val createDate: String? = null,
    @Json(name = "country")
    val country: String? = null,
    @Json(name = "picture")
    val picture: String? = null,
    @Json(name = "wantUsePoints")
    val wantUsePoints: Boolean? = null,
    @Json(name = "surname")
    val surname: String? = null,
    @Json(name = "id")
    val id: String = "",
    @Json(name = "name")
    val name: String = "",
    @Json(name = "canUsePoints")
    val canUsePoints: Long = 0,
)
