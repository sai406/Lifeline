package com.mstech.lifeline.models


import android.annotation.SuppressLint
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class LocationResponse(
    val Location: String,
    val LocationId: Int,
    val LocationInfo: String,
    val Town: String,
    val TownId: Int
) : Parcelable