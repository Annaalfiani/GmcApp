package com.annaalfiani.gmcapps.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Jam(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("jam_tayang") var jamTayang : String? = null
) : Parcelable