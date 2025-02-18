package com.mstech.lifeine.organisation.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrganisationResponse(

	@field:SerializedName("Email")
	val email: String? = null,

	@field:SerializedName("Description")
	val description: String? = null,

	@field:SerializedName("Address")
	val address: String? = null,

	@field:SerializedName("WebsiteLink")
	val websiteLink: String? = null,

	@field:SerializedName("Latitude")
	val latitude: String? = null,

	@field:SerializedName("ModifiedDate")
	val modifiedDate: String? = null,

	@field:SerializedName("YoutubeLink")
	val youtubeLink: String? = null,

	@field:SerializedName("IsApproved")
	val isApproved: Int? = null,

	@field:SerializedName("CompanyNumber")
	val companyNumber: String? = null,

	@field:SerializedName("CreatedDatestring")
	val createdDatestring: String? = null,

	@field:SerializedName("ContactNumber")
	val contactNumber: String? = null,

	@field:SerializedName("TotalRecords")
	val totalRecords: Int? = null,

	@field:SerializedName("FacebookLink")
	val facebookLink: String? = null,

	@field:SerializedName("PayPalBusinessEmail")
	val payPalBusinessEmail: String? = null,

	@field:SerializedName("Password")
	val password: String? = null,

	@field:SerializedName("CountryId")
	val countryId: Int? = null,

	@field:SerializedName("Status")
	val status: Int? = null,

	@field:SerializedName("LogoPath")
	val logoPath: String? = null,

	@field:SerializedName("UserName")
	val userName: String? = null,

	@field:SerializedName("Organisation")
	val organisation: String? = null,

	@field:SerializedName("ModifiedDatestring")
	val modifiedDatestring: String? = null,

	@field:SerializedName("OrganisationId")
	val organisationId: Int? = null,

	@field:SerializedName("Longitude")
	val longitude: String? = null,

	@field:SerializedName("Logo")
	val logo: String? = null,

	@field:SerializedName("CreatedDate")
	val createdDate: String? = null,

	@field:SerializedName("Country")
	val country: String? = null,

	@field:SerializedName("ContactName")
	val contactName: String? = null
) : Parcelable
