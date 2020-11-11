package com.annaalfiani.gmcapps.webservices

import com.annaalfiani.gmcapps.models.Movie
import com.annaalfiani.gmcapps.models.User
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

class ApiCllient {
    //companic object adalah tempat method statis
    //jadi tidak usah instansiate kelas baru unk menggunakannya
    companion object {
        private var retrofit : Retrofit? = null
        const val ENDPOINT = "https://gmcweb.herokuapp.com/"
        private var option = OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
        }.build()


        private fun getClient() :Retrofit {
            return if (retrofit == null) {
                retrofit = Retrofit.Builder().apply {
                    baseUrl(ENDPOINT)
                    addConverterFactory(GsonConverterFactory.create())
                    client(option)
                }.build()
                retrofit!!
            }else{
                retrofit!!
            }
        }

        fun instance() = getClient().create(ApiService::class.java)
    }
}





data class WrappedResponse <T>(
    @SerializedName("message") var message : String,
    @SerializedName("status") var status: Boolean,
    @SerializedName("data") var data : T? = null
)

data class WrappedListResponse <T>(
    @SerializedName("message") var message : String,
    @SerializedName("status") var status: Boolean,
    @SerializedName("data") var data : List<T>
)
