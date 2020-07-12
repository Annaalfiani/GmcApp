package com.annaalfiani.gmcapps.repositories

import com.annaalfiani.gmcapps.models.Movie
import com.annaalfiani.gmcapps.models.MovieSchedule
import com.annaalfiani.gmcapps.utils.ArrayResponse
import com.annaalfiani.gmcapps.utils.SingleResponse
import com.annaalfiani.gmcapps.webservices.ApiService
import com.annaalfiani.gmcapps.webservices.WrappedListResponse
import com.annaalfiani.gmcapps.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface MovieContract {
    fun movieSchedules(movieId: String, listener: ArrayResponse<MovieSchedule>)
    fun movieDetail(movieId : String, listener: SingleResponse<Movie>)
    fun comingSoonMovies(listener: ArrayResponse<Movie>)
    fun nowPlayingMovies(listener: ArrayResponse<Movie>)
}

class MovieRepository (private val api: ApiService) : MovieContract {
    override fun movieSchedules(movieId: String, listener: ArrayResponse<MovieSchedule>) {
        api.movieSchedules(movieId).enqueue(object : Callback<WrappedListResponse<MovieSchedule>>{
            override fun onFailure(call: Call<WrappedListResponse<MovieSchedule>>, t: Throwable) = listener.onFailure(Error(t.message))

            override fun onResponse(call: Call<WrappedListResponse<MovieSchedule>>, response: Response<WrappedListResponse<MovieSchedule>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    override fun movieDetail(movieId: String, listener: SingleResponse<Movie>) {
        api.movieDetail(movieId).enqueue(object : Callback<WrappedResponse<Movie>>{
            override fun onFailure(call: Call<WrappedResponse<Movie>>, t: Throwable) {
                println(t.message)
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Movie>>, response: Response<WrappedResponse<Movie>>) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if(b!!.status){
                            listener.onSuccess(b.data)
                        }else{
                            listener.onFailure(Error("Cannot get movie detail"))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun comingSoonMovies(listener: ArrayResponse<Movie>) {
        api.moviesComingSoon().enqueue(object: Callback<WrappedListResponse<Movie>>{
            override fun onFailure(call: Call<WrappedListResponse<Movie>>, t: Throwable) {
                println(t.message)
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Movie>>, response: Response<WrappedListResponse<Movie>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    override fun nowPlayingMovies(listener: ArrayResponse<Movie>) {
        api.moviesNowPlaying().enqueue(object: Callback<WrappedListResponse<Movie>>{
            override fun onFailure(call: Call<WrappedListResponse<Movie>>, t: Throwable) {
                println(t.message)
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Movie>>, response: Response<WrappedListResponse<Movie>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

}