package com.annaalfiani.gmcapps.webservices

import com.annaalfiani.gmcapps.models.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("api/login")
    fun login(@Field("email") email: String, @Field("password")passord : String): Call<WrappedResponse<User>>

    @GET("api/profil")
    fun profile(@Header("Authorization") token : String) : Call<WrappedResponse<User>>

    @GET("api/film")
    fun movies() : Call<WrappedListResponse<Movie>>

    @GET("api/film/{id}")
    fun movieDetail(@Path("id") movieId: String) : Call<WrappedResponse<Movie>>

    @GET("api/film/nowplaying")
    fun moviesNowPlaying() : Call<WrappedListResponse<Movie>>

    @GET("api/film/comingsoon")
    fun moviesComingSoon() : Call<WrappedListResponse<Movie>>

    @GET("api/film/{id}/jadwaltayang")
    fun movieSchedules(@Path("id") movieId: String) : Call<WrappedListResponse<MovieSchedule>>

    @FormUrlEncoded
    @POST("api/seat/available")
    fun availableSeat(@Header("Authorization") token: String,
                      @Field("id_film") movieId: String,
                      @Field("id_studio") studioId: String,
                      @Field("tanggal") date : String,
                      @Field("jam")hour : String)
            : Call<WrappedResponse<Kursi>>

    @Headers("Content-Type: application/json")
    @POST("api/order")
    fun order(
        @Header("Authorization") token: String,
        @Body body : RequestBody
    ) : Call<WrappedResponse<CreateOrder>>

    @GET("api/order/show")
    fun getMyOrders(
        @Header("Authorization") token: String
    ) : Call<WrappedListResponse<OrderDetails>>

}