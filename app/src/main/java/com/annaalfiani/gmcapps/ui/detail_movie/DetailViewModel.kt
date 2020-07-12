package com.annaalfiani.gmcapps.ui.detail_movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.annaalfiani.gmcapps.models.Movie
import com.annaalfiani.gmcapps.models.MovieSchedule
import com.annaalfiani.gmcapps.repositories.MovieRepository
import com.annaalfiani.gmcapps.utils.ArrayResponse
import com.annaalfiani.gmcapps.utils.SingleLiveEvent
import com.annaalfiani.gmcapps.utils.SingleResponse

class DetailViewModel (private val movieRepo: MovieRepository): ViewModel(){
    private val state: SingleLiveEvent<DetailState> = SingleLiveEvent()
    private val movie = MutableLiveData<Movie>()
    private val schedules = MutableLiveData<HashMap<String, List<MovieSchedule>>>()

    private fun setLoading(){
        state.value = DetailState.Loading(true)
    }
    private fun hideLoading(){
        state.value = DetailState.Loading(false)
    }
    private fun toast(message: String){
        state.value = DetailState.ShowToast(message)
    }

    fun fetchMovieDetail(movieId: String){
        setLoading()
        movieRepo.movieDetail(movieId, object : SingleResponse<Movie>{
            override fun onSuccess(data: Movie?) {
                hideLoading()
                data?.let { movie.postValue(it) }
            }
            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun fetchMovieSchedule(movieId: String){
        setLoading()
        movieRepo.movieSchedules(movieId, object :ArrayResponse<MovieSchedule> {
            override fun onSuccess(datas: List<MovieSchedule>?) {
                hideLoading()
                datas?.let {
                    val temp = it.groupBy { s -> s.date.toString() } as HashMap<String, List<MovieSchedule>>
                    schedules.postValue(temp)
                }
            }
            override fun onFailure(err: Error) {
                hideLoading()
                err.message?.let { toast(it) }
            }
        })
    }

    fun getState() = state
    fun getMovie() = movie
    fun getSchedules() = schedules
}

sealed class DetailState {
    data class Loading(val isLoading: Boolean) : DetailState()
    data class ShowToast(val message: String) : DetailState()
}