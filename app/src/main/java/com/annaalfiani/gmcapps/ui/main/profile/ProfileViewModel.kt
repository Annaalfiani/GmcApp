package com.annaalfiani.gmcapps.ui.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.annaalfiani.gmcapps.models.User
import com.annaalfiani.gmcapps.repositories.UserRepository
import com.annaalfiani.gmcapps.utils.SingleLiveEvent
import com.annaalfiani.gmcapps.utils.SingleResponse

class ProfileViewModel (private val userRepo: UserRepository) : ViewModel(){
    private val state : SingleLiveEvent<ProfileState> = SingleLiveEvent()
    private val currentUser = MutableLiveData<User>()

    private fun setLoading(){
        state.value = ProfileState.Loading(true)
    }
    private fun hideLoading(){
        state.value = ProfileState.Loading(false)
    }
    private fun toast(message: String){
        state.value = ProfileState.ShowToast(message)
    }

    fun fetchProfile(token: String){
        setLoading()
        userRepo.profile(token, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                hideLoading()
                data?.let { currentUser.postValue(it) }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun getState() = state
    fun getCurrentUser() = currentUser
}

sealed class ProfileState {
    data class Loading(val isLoading: Boolean) : ProfileState()
    data class ShowToast(val message: String) : ProfileState()
}