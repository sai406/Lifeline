package com.mstech.lifeline.coordinater.model

import com.google.gson.annotations.SerializedName

data class VolunteerListResponse(

	@field:SerializedName("IsFriend")
	val isFriend: Int? = null,

	@field:SerializedName("ReferredById")
	val referredById: Int? = null,

	@field:SerializedName("Latitude")
	val latitude: Double? = null,

	@field:SerializedName("Gender")
	val gender: Int? = null,

	@field:SerializedName("Createdstring")
	val createdstring: String? = null,

	@field:SerializedName("ModifiedDate")
	val modifiedDate: String? = null,

	@field:SerializedName("CommunityBelong")
	val communityBelong: Any? = null,

	@field:SerializedName("MemberId")
	val memberId: Int? = null,

	@field:SerializedName("RequestSent")
	val requestSent: Int? = null,

	@field:SerializedName("GeoAddress")
	val geoAddress: String? = null,

	@field:SerializedName("MemberInfo")
	val memberInfo: String? = null,

	@field:SerializedName("ProfilePic")
	val profilePic: Any? = null,

	@field:SerializedName("TotalRecords")
	val totalRecords: Int? = null,

	@field:SerializedName("Modifiedstring")
	val modifiedstring: String? = null,

	@field:SerializedName("CountryId")
	val countryId: Int? = null,

	@field:SerializedName("EmailId")
	val emailId: Any? = null,

	@field:SerializedName("IsCoordinator")
	val isCoordinator: Int? = null,

	@field:SerializedName("FirstName")
	val firstName: String? = null,

	@field:SerializedName("CustomerImagePath")
	val customerImagePath: String? = null,

	@field:SerializedName("LocationId")
	val locationId: String? = null,

	@field:SerializedName("Mobile")
	val mobile: String? = null,

	@field:SerializedName("Longitude")
	val longitude: Double? = null,

	@field:SerializedName("Pin")
	val pin: String? = null,

	@field:SerializedName("UserId")
	val userId: String? = null,

	@field:SerializedName("CreatedDate")
	val createdDate: String? = null,

	@field:SerializedName("LastName")
	val lastName: String? = null,

	@field:SerializedName("PostCode")
	val postCode: Any? = null,

	@field:SerializedName("RequestStatus")
	val requestStatus: Any? = null
)
