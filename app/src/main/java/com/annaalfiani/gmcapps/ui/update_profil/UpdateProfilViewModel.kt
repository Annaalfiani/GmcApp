package com.annaalfiani.gmcapps.ui.update_profil

import androidx.lifecycle.ViewModel
import com.annaalfiani.gmcapps.models.User
import com.annaalfiani.gmcapps.repositories.UserRepository
import com.annaalfiani.gmcapps.utils.SingleLiveEvent
import com.annaalfiani.gmcapps.utils.SingleResponse

class UpdateProfilViewModel (private val userRepository: UserRepository) : ViewModel(){
    private val state : SingleLiveEvent<UpdateProfilState> = SingleLiveEvent()

    private fun setLoading(){ state.value = UpdateProfilState.Loading(true) }
    private fun hideLoading(){ state.value = UpdateProfilState.Loading(false) }
    private fun toast(message: String){ state.value = UpdateProfilState.ShowToast(message) }
    private fun success() { state.value = UpdateProfilState.Success }

    fun updateProfil(token : String, name : String, password : String){
        setLoading()
        userRepository.updateProfil(token, name, password, object : SingleResponse<User>{
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

sealed class UpdateProfilState{
    data class Loading(var state : Boolean = false) : UpdateProfilState()
    data class ShowToast(var message : String) : UpdateProfilState()
    object Success : UpdateProfilState()
}