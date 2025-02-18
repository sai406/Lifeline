package com.mstech.lifeline.resources.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.lang.StringBuilder

@Parcelize
data class ResourceResponse(
    val CaseStudyId: StringBuilder?,
    val CreatedDate: String?,
    val CreatedDateString: String?,
    val DocTitle: String?,
    val ResourceBrief: String?,
    val ResourceDoc: String?,
    val ResourceFilePath: String?,
    val ResourceId: Int?,
    val TotalRecords: Int?,
    val VideoUrl: String?
):Parcelable