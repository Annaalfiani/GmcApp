package com.annaalfiani.gmcapps.repositories

import com.annaalfiani.gmcapps.models.Kursi
import com.annaalfiani.gmcapps.utils.SingleResponse
import com.annaalfiani.gmcapps.webservices.ApiService
import com.annaalfiani.gmcapps.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface SeatContract {
    fun getAvailableSeats(
                          movieId: String,
                          studioId: String,
                          date: String,
                          hour: String,
                          listener: SingleResponse<Kursi>)
}

class SeatRepository (private val api: ApiService) : SeatContract {

    override fun getAvailableSeats(movieId : String, studioId: String, date: String, hour: String, listener: SingleResponse<Kursi>) {
        api.availableSeat(movieId, studioId, date, hour).enqueue(object: Callback<WrappedResponse<Kursi>>{
            override fun onFailure(call: Call<WrappedResponse<Kursi>>, t: Throwable) = listener.onFailure(Error(t.message))

            override fun onResponse(call: Call<WrappedResponse<Kursi>>, response: Response<WrappedResponse<Kursi>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

}