package com.annaalfiani.gmcapps.models.v2

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cinema(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("nama_studio") var nama : String? = null
) : Parcelable