package com.annaalfiani.gmcapps.repositories

import com.annaalfiani.gmcapps.models.CreateOrder
import com.annaalfiani.gmcapps.models.OrderDetails
import com.annaalfiani.gmcapps.utils.ArrayResponse
import com.annaalfiani.gmcapps.utils.SingleResponse
import com.annaalfiani.gmcapps.webservices.ApiService
import com.annaalfiani.gmcapps.webservices.WrappedListResponse
import com.annaalfiani.gmcapps.webservices.WrappedResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface OrderContract {
    fun createOrder (token : String, createOrder : CreateOrder, listener : SingleResponse<CreateOrder>)
    fun fetchMyOrders(token : String, listener: ArrayResponse<OrderDetails>)
}

class OrderRepository(private val api : ApiService) : OrderContract {

    override fun createOrder(token: String, createOrder: CreateOrder, listener: SingleResponse<CreateOrder>) {
        val json = Gson().toJson(createOrder)
        println(json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.order("Bearer $token", body).enqueue(object : Callback<WrappedResponse<CreateOrder>>{
            override fun onFailure(call: Call<WrappedResponse<CreateOrder>>, t: Throwable) = listener.onFailure(Error(t.message))

            override fun onResponse(call: Call<WrappedResponse<CreateOrder>>, response: Response<WrappedResponse<CreateOrder>>) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if(b!!.status) listener.onSuccess(b.data) else listener.onFailure(Error(b.message))
                    }
                    else -> {
                        println("r ${response.message()}")
                        listener.onFailure(Error(response.message()))
                    }
                }
            }
        })
    }

    override fun fetchMyOrders(token: String, listener: ArrayResponse<OrderDetails>) {
        api.getMyOrders(token).enqueue(object : Callback<WrappedListResponse<OrderDetails>>{
            override fun onFailure(call: Call<WrappedListResponse<OrderDetails>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<OrderDetails>>, response: Response<WrappedListResponse<OrderDetails>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            println(body.data)
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error())
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }
}