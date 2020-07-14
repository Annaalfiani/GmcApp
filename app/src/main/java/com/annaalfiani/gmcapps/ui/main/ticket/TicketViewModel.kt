package com.annaalfiani.gmcapps.ui.main.ticket

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.annaalfiani.gmcapps.models.OrderDetails
import com.annaalfiani.gmcapps.repositories.OrderRepository
import com.annaalfiani.gmcapps.utils.ArrayResponse
import com.annaalfiani.gmcapps.utils.SingleLiveEvent

class TicketViewModel (private val orderRepository: OrderRepository) : ViewModel(){
    private val state : SingleLiveEvent<TicketState> = SingleLiveEvent()
    private val orderDetails = MutableLiveData<List<OrderDetails>>()

    private fun setLoading(){ state.value = TicketState.IsLoadinng(true)}
    private fun hideLoading(){ state.value = TicketState.IsLoadinng(false)}
    private fun toast(message: String){ state.value = TicketState.ShowToast(message) }

    fun fetchMyOrders(token : String){
        println(token)
        setLoading()
        orderRepository.fetchMyOrders(token, object : ArrayResponse<OrderDetails>{
            override fun onSuccess(datas: List<OrderDetails>?) {
                hideLoading()
                orderDetails.postValue(datas)
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToOrderDetails() = orderDetails

}

sealed class TicketState{
    data class IsLoadinng(var state : Boolean = false) : TicketState()
    data class ShowToast(var message : String) : TicketState()
}