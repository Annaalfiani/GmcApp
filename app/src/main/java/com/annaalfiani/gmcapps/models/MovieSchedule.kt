package com.annaalfiani.gmcapps.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieSchedule(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("harga") var price : Int? = null,
    @SerializedName("date") var date : String? = null,
    @SerializedName("studio") var studio: Studio? = null,
    @SerializedName("hour") var hour: String? = null
) : Parcelable