package com.annaalfiani.gmcapps.models.v2

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Schedulle(
    @SerializedName("tanggal") var date: String? = null,
    @SerializedName("harga") var price: Int? = null,
    @SerializedName("day") var day: String? = null
) : Parcelable