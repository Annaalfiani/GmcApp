package com.annaalfiani.gmcapps.webservices

import com.annaalfiani.gmcapps.models.*
import com.annaalfiani.gmcapps.models.v2.Cinema
import com.annaalfiani.gmcapps.models.v2.Hours
import com.annaalfiani.gmcapps.models.v2.Schedulle
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("api/login")
    fun login(@Field("email") email: String, @Field("password")passord : String): Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("api/register")
    fun register(
        @Field("name") name: String,
        @Field("email")email : String,
        @Field("password") password : String
        //@Field("telp") telp : String
    ): Call<WrappedResponse<User>>

    @GET("api/profil")
    fun profile(@Header("Authorization") token : String) : Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("api/profil/update")
    fun updateProfil(
        @Header("Authorization") token : String,
        @Field("name") name: String,
        @Field("password") password: String
    ) : Call<WrappedResponse<User>>

    @GET("api/v2/film/nowplaying")
    fun moviesNowPlaying() : Call<WrappedListResponse<Movie>>

    @GET("api/v2/film/comingsoon")
    fun moviesComingSoon() : Call<WrappedListResponse<Movie>>

    @GET("api/v2/film/{id}/schedulle")
    fun fetchSchedulle(
        @Path("id") filmId : Int
    ) : Call<WrappedListResponse<Schedulle>>

    @FormUrlEncoded
    @POST("api/v2/schedulle/studio")
    fun fetchStudios(
        @Field("date") date : String,
        @Field("filmId") filmId: Int
    ) : Call<WrappedListResponse<Cinema>>

    @GET("api/v2/schedulle/{dateId}/{studioId}/hours")
    fun fetchHours(
        @Path("dateId") dateId : Int,
        @Path("studioId") studioId : Int
    ) : Call<WrappedListResponse<Hours>>


    @FormUrlEncoded
    @POST("api/v2/seat/available")
    fun availableSeat(
                      @Field("id_film") movieId: String,
                      @Field("id_studio") studioId: String,
                      @Field("tanggal") date : String,
                      @Field("jam")hour : String)
            : Call<WrappedResponse<Kursi>>




    @GET("api/film")
    fun movies() : Call<WrappedListResponse<Movie>>

    @GET("api/film/{id}")
    fun movieDetail(@Path("id") movieId: String) : Call<WrappedResponse<Movie>>

    @GET("api/film/{id}/jadwaltayang")
    fun movieSchedules(@Path("id") movieId: String) : Call<WrappedListResponse<MovieSchedule>>

//    @FormUrlEncoded
//    @POST("api/seat/available")
//    fun availableSeat(@Header("Authorization") token: String,
//                      @Field("id_film") movieId: String,
//                      @Field("id_studio") studioId: String,
//                      @Field("tanggal") date : String,
//                      @Field("jam")hour : String)
//            : Call<WrappedResponse<Kursi>>

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