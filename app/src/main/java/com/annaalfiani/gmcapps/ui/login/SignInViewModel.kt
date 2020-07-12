package com.annaalfiani.gmcapps.ui.login

import androidx.lifecycle.ViewModel
import com.annaalfiani.gmcapps.models.User
import com.annaalfiani.gmcapps.repositories.UserRepository
import com.annaalfiani.gmcapps.utils.SingleLiveEvent
import com.annaalfiani.gmcapps.utils.SingleResponse

class SignInViewModel (private val userRepo: UserRepository) : ViewModel(){
    private val state: SingleLiveEvent<SignInState> = SingleLiveEvent()

    private fun setLoading(){
        state.value = SignInState.Loading(true)
    }
    private fun hideLoading(){
        state.value = SignInState.Loading(false)
    }
    private fun showToast(message: String){
        state.value = SignInState.ShowToast(message)
    }
    private fun showAlert(message: String){
        state.value = SignInState.ShowAlert(message)
    }
    private fun success(token: String){
        state.value = SignInState.Success(token)
    }

    fun login(email: String, password: String){
        setLoading()
        userRepo.login(email, password, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                hideLoading()
                data?.let { success(it.token.toString()) }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                showAlert(err.message.toString())
            }
        })
    }

    fun getState() = state
}

sealed class SignInState {
    data class Loading(val isLoading: Boolean) : SignInState()
    data class ShowToast(val message: String) : SignInState()
    data class ShowAlert(val message: String) : SignInState()
    data class Success(val token: String) : SignInState()
}