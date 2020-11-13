package com.annaalfiani.gmcapps.ui.detail_film

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.annaalfiani.gmcapps.models.v2.Cinema
import com.annaalfiani.gmcapps.models.v2.Hours
import com.annaalfiani.gmcapps.models.v2.Schedulle
import com.annaalfiani.gmcapps.repositories.SchedulleRepository
import com.annaalfiani.gmcapps.utils.ArrayResponse
import com.annaalfiani.gmcapps.utils.SingleLiveEvent

class DetailFilmViewModel (private val schedulleRepository: SchedulleRepository) : ViewModel(){
    private val state : SingleLiveEvent<DetailFilmState> = SingleLiveEvent()
    private val schedulles = MutableLiveData<List<Schedulle>>()
    private val cinemas = MutableLiveData<List<Cinema>>()
    private val hours = MutableLiveData<List<Hours>>()
    private val dateId = MutableLiveData<String>()

    private fun isLoading(b : Boolean){ state.value = DetailFilmState.Loading(b) }
    private fun toast(m : String){ state.value = DetailFilmState.ShowToast(m) }

    fun fetchSchedulles(filmId : String){
        schedulleRepository.fetchScedulles(filmId, object : ArrayResponse<Schedulle>{
            override fun onSuccess(datas: List<Schedulle>?) {
                isLoading(false)
                datas?.let { schedulles.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun fetchStudios(date : String , filmId: String){
        isLoading(true)
        schedulleRepository.fetchStudios(date, filmId, object : ArrayResponse<Cinema>{
            override fun onSuccess(datas: List<Cinema>?) {
                isLoading(false)
                datas?.let { cinemas.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun fetchHours(dateId : String, studioId : String){
        isLoading(true)
        schedulleRepository.fetchHours(dateId, studioId, object : ArrayResponse<Hours>{
            override fun onSuccess(datas: List<Hours>?) {
                isLoading(false)
                datas?.let { hours.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun setDateId(id: String){
        dateId.value = id
    }

    fun listenToState() = state
    fun listenToSchedulles() = schedulles
    fun listenToStudios() = cinemas
    fun listenToHours() = hours
    fun getDateId() = dateId
}

sealed class DetailFilmState{
    data class Loading(var state : Boolean = false) : DetailFilmState()
    data class ShowToast(var message : String) : DetailFilmState()
}