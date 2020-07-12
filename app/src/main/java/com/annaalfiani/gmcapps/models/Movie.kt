package com.annaalfiani.gmcapps.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie (
    @SerializedName("id") var id: Int? = null,
    @SerializedName("foto") var foto: String? = null,
    @SerializedName("judul") var judul: String? = null,
    @SerializedName("sinopsis") var sinopsis: String? = null,
    @SerializedName("genre") var genre: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("jadwaltayang") var jadwalTayang : JadwalTayang? = null
): Parcelable