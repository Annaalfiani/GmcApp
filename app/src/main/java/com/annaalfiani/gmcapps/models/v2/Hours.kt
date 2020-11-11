package com.annaalfiani.gmcapps.models.v2

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hours(
    @SerializedName("id") val id : Int? = null,
    @SerializedName("jam") val hour: String? = null
) : Parcelable