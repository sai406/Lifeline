package com.mstech.lifeline.organisation.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ArticleResponse(

	@field:SerializedName("Status")
	val status: Int? = null,

	@field:SerializedName("StatusDisplay")
	val statusDisplay: String? = null,

	@field:SerializedName("OrganisationId")
	val organisationId: Int? = null,

	@field:SerializedName("CreatedDate")
	val createdDate: String? = null,

	@field:SerializedName("ModifiedDateDisplay")
	val modifiedDateDisplay: String? = null,

	@field:SerializedName("Title")
	val title: String? = null,

	@field:SerializedName("ArticleId")
	val articleId: Int? = null,

	@field:SerializedName("ModifiedDate")
	val modifiedDate: String? = null,

	@field:SerializedName("CreatedDateDisplay")
	val createdDateDisplay: String? = null
) : Parcelable
