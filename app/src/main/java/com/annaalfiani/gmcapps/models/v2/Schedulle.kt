package com.annaalfiani.gmcapps.models.v2

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Schedulle(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("tanggal") var date: String? = null,
    @SerializedName("harga") var price: Int? = null
) : Parcelable