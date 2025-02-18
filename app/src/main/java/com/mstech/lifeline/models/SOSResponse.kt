package com.mstech.lifeline.models

import com.google.gson.annotations.SerializedName

data class SOSResponse(

	@field:SerializedName("HelpId")
	val helpId: Int? = null,

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("FirstName")
	val firstName: String? = null,

	@field:SerializedName("CustomerImagePath")
	val customerImagePath: String? = null,

	@field:SerializedName("Latitude")
	val latitude: Double? = null,

	@field:SerializedName("Image")
	val image: String? = null,

	@field:SerializedName("Longitude")
	val longitude: Double? = null,

	@field:SerializedName("MemberId")
	val memberId: Int? = null,

	@field:SerializedName("GeoAddress")
	val geoAddress: String? = null,

	@field:SerializedName("ImagePath")
	val imagePath: String? = null,

	@field:SerializedName("CreatedDate")
	val createdDate: String? = null,

	@field:SerializedName("ProfilePic")
	val profilePic: Any? = null,

	@field:SerializedName("LastName")
	val lastName: String? = null,

	@field:SerializedName("CoordinatorId")
	val coordinatorId: Int? = null,

	@field:SerializedName("CreateDatestring")
	val createDatestring: String? = null,

	@field:SerializedName("Location")
	val location: String? = null
)
