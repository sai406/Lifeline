package com.mstech.lifeline.models

import com.google.gson.annotations.SerializedName

data class MobileResponse(

    @field:SerializedName("StatusCode")
    val statusCode: Int? = null,

    @field:SerializedName("StatusMessage")
    val statusMessage: String? = null,

    @field:SerializedName("IsVolunteer")
    val isVolunteer: String? = null,

    @field:SerializedName("HelpLineNumber")
    val helpLineNumber: String? = null,

    @field:SerializedName("CoordinatorNumber")
    val coordinatorNumber: String? = null
)
