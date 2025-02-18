package com.mstech.lifeline.models

import com.google.gson.annotations.SerializedName

data class VerificationResponse(

	@field:SerializedName("StatusCode")
	val statusCode: Int? = null,

	@field:SerializedName("StatusMessage")
	val statusMessage: String? = null
)
