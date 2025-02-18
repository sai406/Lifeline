package com.mstech.lifeline.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CampaignListResponse(

	@field:SerializedName("SpecialInstructions")
	val specialInstructions: String? = null,

	@field:SerializedName("EndTime")
	val endTime: String? = null,

	@field:SerializedName("CampaignTitle")
	val campaignTitle: String? = null,

	@field:SerializedName("LocationId")
	val locationId: Int? = null,

	@field:SerializedName("StartTime")
	val startTime: String? = null,

	@field:SerializedName("Latitude")
	val latitude: Double? = null,

	@field:SerializedName("Timestring")
	val timestring: String? = null,

	@field:SerializedName("Image")
	val image: String? = null,

	@field:SerializedName("Longitude")
	val longitude: Double? = null,

	@field:SerializedName("CampaignInfo")
	val campaignInfo: String? = null,

	@field:SerializedName("MemberId")
	val memberId: Int? = null,

	@field:SerializedName("CampaignId")
	val campaignId: Int? = null,

	@field:SerializedName("ImagePath")
	val imagePath: String? = null,

	@field:SerializedName("GeoLocation")
	val geoLocation: String? = null,

	@field:SerializedName("Datestring")
	val datestring: String? = null
) : Parcelable
