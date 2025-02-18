package com.mstech.lifeline.coordinater.model

import com.google.gson.annotations.SerializedName

data class RescueResponse(

    @field:SerializedName("StatusCode")
    val statusCode: Int? = null,

    @field:SerializedName("StatusMessage")
    val statusMessage: String? = null,

    @field:SerializedName("Latitude")
    val latitude: String? = null,

    @field:SerializedName("Longitude")
    val longitude: String? = null,

    @field:SerializedName("PostCode")
    val postcode: String? = null,
)
