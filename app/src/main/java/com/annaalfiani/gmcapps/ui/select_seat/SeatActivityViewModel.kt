package com.annaalfiani.gmcapps.ui.select_seat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.annaalfiani.gmcapps.models.Movie
import com.annaalfiani.gmcapps.models.Kursi
import com.annaalfiani.gmcapps.models.Seat
import com.annaalfiani.gmcapps.utils.SingleLiveEvent

class SeatActivityViewModel : ViewModel(){
    private val state : SingleLiveEvent<SeatActivityState> = SingleLiveEvent()
    private val seatInfo = MutableLiveData<Kursi>()
    private val selectedSeats = MutableLiveData<MutableList<Seat>>(mutableListOf())

    private fun showToast(message: String){ state.value =
        SeatActivityState.ShowToast(
            message
        )
    }

    fun setSeatInfo(seat: Kursi){
        seatInfo.value = seat
    }

    fun addSelectedSeat(seat: Seat){
        selectedSeats.value!!.add(seat)
    }

    fun releaseSeat(seat: Seat){
        selectedSeats.value!!.remove(seat)
    }


    fun listenToSeatInfo() = seatInfo
    fun listenToState() = state
    fun getSelectedSeats() = selectedSeats
}

sealed class SeatActivityState {
    data class Loading(val isLoading: Boolean) : SeatActivityState()
    data class ShowToast(val message: String) : SeatActivityState()
}