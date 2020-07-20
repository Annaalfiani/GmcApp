package com.annaalfiani.gmcapps.ui.update_profil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.extensions.gone
import com.annaalfiani.gmcapps.extensions.showToast
import com.annaalfiani.gmcapps.extensions.visible
import com.annaalfiani.gmcapps.models.User
import com.annaalfiani.gmcapps.utils.Utilities
import com.annaalfiani.gmcapps.utils.extensions.toast
import kotlinx.android.synthetic.main.activity_update_profil.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdateProfilActivity : AppCompatActivity() {

    private val updateProfilViewModel : UpdateProfilViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profil)

        observer()
        updateProfil()
        setUser()
    }

    private fun observer() {
        updateProfilViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    }

    private fun handleUiState(it: UpdateProfilState) {
        when(it){
            is UpdateProfilState.Loading -> successLoading(it.state)
            is UpdateProfilState.ShowToast -> showToast(it.message)
            is UpdateProfilState.Success -> handleSuccess()
        }
    }

    private fun successLoading(state: Boolean) {
        if (state){
            pb_profile.visible()
        }else{
            pb_profile.gone()
        }
        btn_simpan_profil.isEnabled = !state
    }

    private fun handleSuccess() {
        toast("berhasil update profil")
        finish()
    }

    private fun updateProfil(){
        btn_simpan_profil.setOnClickListener {
            val token  = "Bearer ${Utilities.getToken(this@UpdateProfilActivity)}"
            val name = edit_name.text.toString().trim()
            val pass = editpassword.text.toString().trim()
            val telp = edit_telp.text.toString().trim()
            updateProfilViewModel.updateProfil(token!!, name, pass, telp)
        }
    }

    private fun setUser(){
        getPassedUser()?.let {
            edit_name.setText(it.name)
            edit_telp.setText(it.telp)
            edit_email.setText(it.email)
        }
    }

    private fun getPassedUser() : User? = intent.getParcelableExtra("USER")
}
