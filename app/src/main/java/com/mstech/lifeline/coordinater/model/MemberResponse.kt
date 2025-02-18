package com.mstech.lifeline.coordinater.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MemberResponse(

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
	val communityBelong: String? = null,

	@field:SerializedName("MemberId")
	val memberId: Int? = null,

	@field:SerializedName("RequestSent")
	val requestSent: Int? = null,

	@field:SerializedName("GeoAddress")
	val geoAddress: String? = null,

	@field:SerializedName("MemberInfo")
	val memberInfo: String? = null,

	@field:SerializedName("ProfilePic")
	val profilePic: String? = null,

	@field:SerializedName("TotalRecords")
	val totalRecords: Int? = null,

	@field:SerializedName("Modifiedstring")
	val modifiedstring: String? = null,

	@field:SerializedName("CountryId")
	val countryId: Int? = null,

	@field:SerializedName("Status")
	val status: Int? = null,

	@field:SerializedName("EmailId")
	val emailId: String? = null,

	@field:SerializedName("IsCoordinator")
	val isCoordinator: Int? = null,

	@field:SerializedName("FirstName")
	val firstName: String? = null,

	@field:SerializedName("CustomerImagePath")
	val customerImagePath: String? = null,

	@field:SerializedName("LocationId")
	val locationId: Int? = null,

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
	val postCode: String? = null,

	@field:SerializedName("Location")
	val location: String? = null,

	@field:SerializedName("RequestStatus")
	val requestStatus: String? = null
) : Parcelable
