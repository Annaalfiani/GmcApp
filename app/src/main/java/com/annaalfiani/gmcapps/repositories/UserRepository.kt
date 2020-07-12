package com.annaalfiani.gmcapps.repositories

import com.annaalfiani.gmcapps.models.User
import com.annaalfiani.gmcapps.utils.SingleResponse
import com.annaalfiani.gmcapps.webservices.ApiService
import com.annaalfiani.gmcapps.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface UserContract {
    fun profile(token: String, listener: SingleResponse<User>)
    fun login(email: String, password: String, listener: SingleResponse<User>)
}

class UserRepository (private val api : ApiService) : UserContract {
    override fun profile(token: String, listener: SingleResponse<User>) {
        api.profile(token).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println(t.message)
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if(b!!.status){
                            listener.onSuccess(b.data)
                        }else{
                            listener.onFailure(Error(b.message))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    override fun login(email: String, password: String, listener: SingleResponse<User>) {
        api.login(email, password).enqueue(object: Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println(t.message)
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if(b!!.status){
                            listener.onSuccess(b.data)
                        }else{
                            listener.onFailure(Error("Kombinasi email dan password salah"))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }
}