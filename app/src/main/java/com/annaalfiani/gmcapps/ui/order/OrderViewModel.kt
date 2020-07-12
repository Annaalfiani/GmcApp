package com.annaalfiani.gmcapps.ui.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.annaalfiani.gmcapps.models.CreateOrder
import com.annaalfiani.gmcapps.models.Kursi
import com.annaalfiani.gmcapps.models.Seat
import com.annaalfiani.gmcapps.models.User
import com.annaalfiani.gmcapps.repositories.MovieRepository
import com.annaalfiani.gmcapps.repositories.OrderRepository
import com.annaalfiani.gmcapps.repositories.SeatRepository
import com.annaalfiani.gmcapps.repositories.UserRepository
import com.annaalfiani.gmcapps.utils.SingleLiveEvent
import com.annaalfiani.gmcapps.utils.SingleResponse

class OrderViewModel (private val movieRepo: MovieRepository,
                      private val seatRepository: SeatRepository,
                      private val orderRepository: OrderRepository,
                      private val userRepository: UserRepository) : ViewModel(){
    private val state: SingleLiveEvent<OrderState> = SingleLiveEvent()
    private val seats = MutableLiveData<Kursi?>(null)
    private val rowNames = MutableLiveData<HashMap<String, String>>()
    private val selectedSeats = MutableLiveData<List<Seat>>()
    private val user = MutableLiveData<User>()

    private fun setLoading(){ state.value = OrderState.Loading(true) }
    private fun hideLoading(){ state.value = OrderState.Loading(false) }
    private fun toast(message: String){ state.value = OrderState.ShowToast(message) }
    private fun alert(message: String){ state.value = OrderState.Alert(message) }
    private fun successOrder(){ state.value = OrderState.SuccessOrder }


    fun fetchSeats(token: String, date: String, hour: String, studioId : String){
        setLoading()
        seatRepository.getAvailableSeats(token, studioId, hour, date, object : SingleResponse<Kursi>{
            override fun onSuccess(data: Kursi?) {
                hideLoading()
                data?.let {
                    println("Att sukes")
                    seats.postValue(it)
                }
            }
            override fun onFailure(err: Error) {
                hideLoading()
                err.message?.let { toast(it) }
            }
        })
    }

    fun fetchProfile(token: String){
        println(token)
        setLoading()
        userRepository.profile(token, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                hideLoading()
                data?.let { user.postValue(it) }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun createOrder(token : String, createOrder: CreateOrder){
        setLoading()
        orderRepository.createOrder(token, createOrder, object: SingleResponse<CreateOrder>{
            override fun onSuccess(data: CreateOrder?) {
                hideLoading()
                data?.let { successOrder() }
            }
            override fun onFailure(err: Error) {
                hideLoading()
                err.message?.let { toast(it) }
            }

        })
    }

    fun setSelectedSeats(seats: List<Seat>){
        selectedSeats.value = seats
    }

    fun getState() = state
    fun getSeats() = seats
    fun getSelectedSeats() = selectedSeats
    fun getUser() = user

}

sealed class OrderState {
    object SuccessOrder : OrderState()
    data class Alert(val message: String) : OrderState()
    data class Loading(val isLoading: Boolean) : OrderState()
    data class ShowToast(val message: String) : OrderState()
}