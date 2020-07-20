package com.annaalfiani.gmcapps.ui.register

import androidx.lifecycle.ViewModel
import com.annaalfiani.gmcapps.models.User
import com.annaalfiani.gmcapps.repositories.UserRepository
import com.annaalfiani.gmcapps.utils.SingleLiveEvent
import com.annaalfiani.gmcapps.utils.SingleResponse

class SignUpViewModel (private val  userRepository: UserRepository) : ViewModel(){
    private val state : SingleLiveEvent<SignUpState> = SingleLiveEvent()


    private fun setLoading(){ state.value = SignUpState.Loading(true) }
    private fun hideLoading(){ state.value = SignUpState.Loading(false) }
    private fun toast(message: String){ state.value = SignUpState.ShowToast(message) }
    private fun success() { state.value = SignUpState.Success }

    fun register(name : String, email : String, password : String, telp : String){
        hideLoading()
        userRepository.register(name, email, password, telp, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                hideLoading()
                success()
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
}

sealed class SignUpState{
    data class Loading(var state : Boolean = false) : SignUpState()
    data class ShowToast(var message : String) : SignUpState()
    object Success : SignUpState()
}