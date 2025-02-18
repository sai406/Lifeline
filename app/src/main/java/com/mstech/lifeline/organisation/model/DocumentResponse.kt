package com.mstech.lifeline.organisation.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DocumentResponse(

	@field:SerializedName("Status")
	val status: Int? = null,

	@field:SerializedName("StatusDisplay")
	val statusDisplay: String? = null,

	@field:SerializedName("Article")
	val article: String? = null,

	@field:SerializedName("OrganisationId")
	val organisationId: Int? = null,

	@field:SerializedName("Title")
	val title: String? = null,

	@field:SerializedName("ModifiedDate")
	val modifiedDate: String? = null,

	@field:SerializedName("CreatedDateDisplay")
	val createdDateDisplay: String? = null,

	@field:SerializedName("DocumentDetails")
	val documents: List<DocumentsItem?>? = null,

	@field:SerializedName("CreatedDate")
	val createdDate: String? = null,

	@field:SerializedName("ModifiedDateDisplay")
	val modifiedDateDisplay: String? = null,

	@field:SerializedName("LinksDetails")
	val links: List<LinksItem?>? = null,

	@field:SerializedName("TotalRecords")
	val totalRecords: Int? = null,

	@field:SerializedName("ArticleId")
	val articleId: Int? = null,

	@field:SerializedName("Multidocument")
	val multidocument: List<String?>? = null
) : Parcelable

@Parcelize
data class LinksItem(

	@field:SerializedName("CreatedDate")
	val createdDate: String? = null,

	@field:SerializedName("ArticleId")
	val articleId: Int? = null,

	@field:SerializedName("LinkId")
	val linkId: Int? = null,

	@field:SerializedName("Link")
	val link: String? = null,

	@field:SerializedName("CreatedDateDisplay")
	val createdDateDisplay: String? = null
) : Parcelable

@Parcelize
data class DocumentsItem(

	@field:SerializedName("DocumentId")
	val documentId: Int? = null,

	@field:SerializedName("CreatedDate")
	val createdDate: String? = null,

	@field:SerializedName("DocumentPath")
	val documentPath: String? = null,

	@field:SerializedName("ArticleId")
	val articleId: Int? = null,

	@field:SerializedName("Document")
	val document: String? = null,

	@field:SerializedName("CreatedDateDisplay")
	val createdDateDisplay: String? = null
) : Parcelable
