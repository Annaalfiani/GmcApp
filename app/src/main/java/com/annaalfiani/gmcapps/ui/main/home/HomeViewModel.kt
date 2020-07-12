package com.annaalfiani.gmcapps.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.annaalfiani.gmcapps.models.Movie
import com.annaalfiani.gmcapps.repositories.MovieRepository
import com.annaalfiani.gmcapps.utils.ArrayResponse
import com.annaalfiani.gmcapps.utils.SingleLiveEvent

class HomeViewModel (private val movieRepo: MovieRepository) : ViewModel(){
    private val state : SingleLiveEvent<HomeState> = SingleLiveEvent()
    private val comingSoonMovies = MutableLiveData<List<Movie>>()
    private val nowPlayingMovies = MutableLiveData<List<Movie>>()

    private fun setLoadingNowPlaying(){
        state.value = HomeState.LoadingNowPlaying(true)
    }
    private fun hideLoadingNowPlaying(){
        state.value = HomeState.LoadingNowPlaying(false)
    }

    private fun setLoadingComingSoon(){
        state.value = HomeState.LoadingComingSoon(true)
    }
    private fun hideLoadingComingSoon(){
        state.value = HomeState.LoadingComingSoon(false)
    }

    private fun toast(message: String){
        state.value = HomeState.ShowToast(message)
    }

    fun fetchComingSoonMovies(){
        setLoadingComingSoon()
        movieRepo.comingSoonMovies(object : ArrayResponse<Movie>{
            override fun onSuccess(datas: List<Movie>?) {
                hideLoadingComingSoon()
                datas?.let { comingSoonMovies.postValue(it) }
            }

            override fun onFailure(err: Error) {
                hideLoadingComingSoon()
                toast(err.message.toString())
            }
        })
    }

    fun fetchNowPlayingMovies(){
        setLoadingNowPlaying()
        movieRepo.nowPlayingMovies(object: ArrayResponse<Movie>{
            override fun onSuccess(datas: List<Movie>?) {
                hideLoadingNowPlaying()
                datas?.let { nowPlayingMovies.postValue(it) }
            }

            override fun onFailure(err: Error) {
                hideLoadingNowPlaying()
                toast(err.message.toString())
            }
        })
    }

    fun getState() = state
    fun getComingSoonMovies() = comingSoonMovies
    fun getNowPlayingMovies() = nowPlayingMovies
}

sealed class HomeState {
    data class LoadingNowPlaying(val isLoading: Boolean) : HomeState()
    data class LoadingComingSoon(val isLoading: Boolean) : HomeState()
    data class ShowToast(val message: String) : HomeState()
}