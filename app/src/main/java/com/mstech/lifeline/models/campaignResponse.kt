package com.mstech.lifeline.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CampaignResponse(

	@field:SerializedName("NewCampaigns")
	val newCampaigns: MutableList<NewCampaignsItem>,

	@field:SerializedName("Completed")
	val completed: MutableList<NewCampaignsItem>,

	@field:SerializedName("signed")
	val signed: MutableList<NewCampaignsItem>
) : Parcelable

@Parcelize
data class CompletedItem(

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

	@field:SerializedName("TotalRecords")
	val totalRecords: Int? = null,

	@field:SerializedName("GeoLocation")
	val geoLocation: String? = null,

	@field:SerializedName("Datestring")
	val datestring: String? = null,

	@field:SerializedName("Location")
	val location: String? = null
) : Parcelable

@Parcelize
data class SignedItem(

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

	@field:SerializedName("TotalRecords")
	val totalRecords: Int? = null,

	@field:SerializedName("GeoLocation")
	val geoLocation: String? = null,

	@field:SerializedName("Datestring")
	val datestring: String? = null,

	@field:SerializedName("Location")
	val location: String? = null
) : Parcelable

@Parcelize
data class NewCampaignsItem(

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

	@field:SerializedName("TotalRecords")
	val totalRecords: Int? = null,

	@field:SerializedName("GeoLocation")
	val geoLocation: String? = null,

	@field:SerializedName("Datestring")
	val datestring: String? = null,

	@field:SerializedName("Location")
	val location: String? = null
) : Parcelable
