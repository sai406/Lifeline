package com.mstech.lifeline.coordinater.model

import com.google.gson.annotations.SerializedName

data class StatusResponse(

	@field:SerializedName("StatusCode")
	val statusCode: Int? = null,

	@field:SerializedName("StatusMessage")
	val statusMessage: String? = null
)
