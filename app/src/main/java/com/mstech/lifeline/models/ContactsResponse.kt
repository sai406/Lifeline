package com.mstech.lifeline.models

import com.google.gson.annotations.SerializedName

data class ContactsResponse(

	@field:SerializedName("TownCoordinators")
	val townCoordinators: List<Any?>? = null,

	@field:SerializedName("AreaCoordinators")
	val areaCoordinators: List<AreaCoordinatorsItem?>? = null,

	@field:SerializedName("SOScontacts")
	val sOScontacts: List<SOScontactsItem?>? = null,

	@field:SerializedName("AreaVolunteers")
	val areaVolunteers: List<Any?>? = null
)

data class AreaCoordinatorsItem(

	@field:SerializedName("CountryId")
	val countryId: Int? = null,

	@field:SerializedName("EmailId")
	val emailId: String? = null,

	@field:SerializedName("FirstName")
	val firstName: String? = null,

	@field:SerializedName("LocationId")
	val locationId: String? = null,

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

	@field:SerializedName("Mobile")
	val mobile: String? = null,

	@field:SerializedName("Longitude")
	val longitude: Double? = null,

	@field:SerializedName("CommunityBelong")
	val communityBelong: Any? = null,

	@field:SerializedName("MemberId")
	val memberId: Int? = null,

	@field:SerializedName("GeoAddress")
	val geoAddress: String? = null,

	@field:SerializedName("Pin")
	val pin: String? = null,

	@field:SerializedName("MemberInfo")
	val memberInfo: Any? = null,

	@field:SerializedName("UserId")
	val userId: String? = null,

	@field:SerializedName("CreatedDate")
	val createdDate: String? = null,

	@field:SerializedName("TotalRecords")
	val totalRecords: Int? = null,

	@field:SerializedName("LastName")
	val lastName: String? = null,

	@field:SerializedName("PostCode")
	val postCode: String? = null,

	@field:SerializedName("Modifiedstring")
	val modifiedstring: String? = null
)

data class SOScontactsItem(

	@field:SerializedName("MemberId")
	val memberId: Int? = null,

	@field:SerializedName("ContactId")
	val contactId: Int? = null,

	@field:SerializedName("EmailId")
	val emailId: String? = null,

	@field:SerializedName("Mobile")
	val mobile: String? = null,

	@field:SerializedName("Name")
	val name: String? = null
)
