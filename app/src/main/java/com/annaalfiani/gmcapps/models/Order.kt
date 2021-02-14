package com.annaalfiani.gmcapps.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreateOrder(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("id_studio") var id_studio : Int? = null,
    @SerializedName("id_film") var id_film : Int? = null,
    @SerializedName("id_jadwal_tayang") var id_jadwal_tayang : Int? = null,
    @SerializedName("tanggal") var tanggal : String? = null,
    @SerializedName("jam") var jam : String ? = null,
    @SerializedName("harga") var harga : Int ? = null,
    @SerializedName("kursi") var seats : List<Seat> = mutableListOf()
) : Parcelable


@Parcelize
data class Order(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("studio") var studio : Studio,
    @SerializedName("film") var film : Movie,
    @SerializedName("jadwal_tayang") var jadwal: Jadwal,
    @SerializedName("tanggal") var tanggal : String? = null,
    @SerializedName("jam") var jam : String ? = null,
    @SerializedName("harga") var harga : Int ? = null
    //@SerializedName("kursi") var seats : List<Seat> = mutableListOf()
) : Parcelable

@Parcelize
data class OrderDetails(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("price") var price : Int? = null,
    @SerializedName("order") var order : Order,
    @SerializedName("kursi") var kursi : Seat
) : Parcelable