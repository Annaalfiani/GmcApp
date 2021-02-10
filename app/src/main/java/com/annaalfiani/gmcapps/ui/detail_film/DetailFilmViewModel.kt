package com.annaalfiani.gmcapps.ui.detail_film

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.annaalfiani.gmcapps.models.CreateOrder
import com.annaalfiani.gmcapps.models.Kursi
import com.annaalfiani.gmcapps.models.Seat
import com.annaalfiani.gmcapps.models.User
import com.annaalfiani.gmcapps.models.v2.Cinema
import com.annaalfiani.gmcapps.models.v2.Hours
import com.annaalfiani.gmcapps.models.v2.Schedulle
import com.annaalfiani.gmcapps.repositories.OrderRepository
import com.annaalfiani.gmcapps.repositories.SchedulleRepository
import com.annaalfiani.gmcapps.repositories.SeatRepository
import com.annaalfiani.gmcapps.repositories.UserRepository
import com.annaalfiani.gmcapps.ui.order.OrderState
import com.annaalfiani.gmcapps.utils.ArrayResponse
import com.annaalfiani.gmcapps.utils.SingleLiveEvent
import com.annaalfiani.gmcapps.utils.SingleResponse

class DetailFilmViewModel (private val schedulleRepository: SchedulleRepository,
                           private val seatRepository: SeatRepository,
                           private val userRepository: UserRepository,
                           private val orderRepository: OrderRepository) : ViewModel(){
    private val state : SingleLiveEvent<DetailFilmState> = SingleLiveEvent()
    private val schedulles = MutableLiveData<List<Schedulle>>()
    private val cinemas = MutableLiveData<List<Cinema>>()
    private val hours = MutableLiveData<List<Hours>>()
    private val seats = MutableLiveData<Kursi?>(null)
    private val selectedSeats = MutableLiveData<List<Seat>>()

    private val date = MutableLiveData<Schedulle>()
    private val user = MutableLiveData<User>()
    private val hour = MutableLiveData<Hours>()
    private val cinema = MutableLiveData<Cinema>()


    private fun isLoading(b : Boolean){ state.value = DetailFilmState.Loading(b) }
    private fun toast(m : String){ state.value = DetailFilmState.ShowToast(m) }
    private fun successOrder(){ state.value = DetailFilmState.SuccessOrder }

    fun fetchSeats(moviedId: String, studioId : String, date: String, hour: String){
        isLoading(true)
        seatRepository.getAvailableSeats(moviedId, studioId, date, hour, object :
            SingleResponse<Kursi> {
            override fun onSuccess(data: Kursi?) {
                isLoading(false)
                data?.let {
                    println("Att sukes")
                    seats.postValue(it)
                }
            }
            override fun onFailure(err: Error) {
                isLoading(false)
                err.message?.let { toast(it) }
            }
        })
    }

    fun fetchSchedulles(filmId : String){
        isLoading(true)
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

    fun fetchProfile(token: String){
        isLoading(true)
        userRepository.profile(token, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                isLoading(false)
                data?.let { user.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun createOrder(token : String, createOrder: CreateOrder){
        isLoading(true)
        orderRepository.createOrder(token, createOrder, object: SingleResponse<CreateOrder>{
            override fun onSuccess(data: CreateOrder?) {
                isLoading(false)
                data?.let {
                    successOrder()
                }
            }
            override fun onFailure(err: Error) {
                isLoading(false)
                err.message?.let { toast(it) }
            }

        })
    }

    fun setSelectedSeats(seats: List<Seat>){
        selectedSeats.value = seats
    }

    fun setSelectedDate(d: Schedulle){
        date.value = d
    }

    fun setSelectedHour(h: Hours){
        hour.value = h
    }

    fun setSelectedCinema(c: Cinema){
        cinema.value = c
    }

    fun listenToState() = state
    fun listenToSchedulles() = schedulles
    fun listenToStudios() = cinemas
    fun listenToHours() = hours
    fun getSeats() = seats
    fun getSelectedSeats() = selectedSeats
    fun getSelectedCinema() = cinema
    fun getSelectedDate() = date
    fun getSelectedHour() = hour
    fun getUser() = user
}

sealed class DetailFilmState{
    data class Loading(var state : Boolean = false) : DetailFilmState()
    data class ShowToast(var message : String) : DetailFilmState()
    object SuccessOrder : DetailFilmState()
}