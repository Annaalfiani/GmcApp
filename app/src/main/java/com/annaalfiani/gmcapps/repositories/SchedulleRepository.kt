package com.annaalfiani.gmcapps.repositories

import com.annaalfiani.gmcapps.models.v2.Cinema
import com.annaalfiani.gmcapps.models.v2.Hours
import com.annaalfiani.gmcapps.models.v2.Schedulle
import com.annaalfiani.gmcapps.utils.ArrayResponse
import com.annaalfiani.gmcapps.webservices.ApiService
import com.annaalfiani.gmcapps.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface SchedulleContract{
    fun fetchScedulles(filmId : String, listener : ArrayResponse<Schedulle>)
    fun fetchStudios(date : String, listener: ArrayResponse<Cinema>)
    fun fetchHours(dateId : String, studioId : String, listener: ArrayResponse<Hours>)
}


class SchedulleRepository (private val api : ApiService) : SchedulleContract{

    override fun fetchScedulles(filmId: String, listener: ArrayResponse<Schedulle>) {
        api.fetchSchedulle(filmId.toInt()).enqueue(object : Callback<WrappedListResponse<Schedulle>>{
            override fun onFailure(call: Call<WrappedListResponse<Schedulle>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Schedulle>>, response: Response<WrappedListResponse<Schedulle>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    override fun fetchStudios(date: String, listener: ArrayResponse<Cinema>) {
        api.fetchStudios(date).enqueue(object : Callback<WrappedListResponse<Cinema>>{
            override fun onFailure(call: Call<WrappedListResponse<Cinema>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Cinema>>, response: Response<WrappedListResponse<Cinema>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun fetchHours(dateId: String, studioId: String, listener: ArrayResponse<Hours>) {
        api.fetchHours(dateId.toInt(), studioId.toInt()).enqueue(object : Callback<WrappedListResponse<Hours>>{
            override fun onFailure(call: Call<WrappedListResponse<Hours>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Hours>>, response: Response<WrappedListResponse<Hours>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}