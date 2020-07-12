package com.annaalfiani.gmcapps.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class JadwalTayang(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("harga") var harga : Int? = null,
    @SerializedName("tanggal_mulai") var tanggalMulai : String? = null,
    @SerializedName("tanggal_selesai") var tanggalSelesai : String? = null,
    @SerializedName("studio") var studio: Studio? = null,
    @SerializedName("jam") var jam : String? = null
    //@SerializedName("jam") var jams : List<Jam> = mutableListOf()
) : Parcelable