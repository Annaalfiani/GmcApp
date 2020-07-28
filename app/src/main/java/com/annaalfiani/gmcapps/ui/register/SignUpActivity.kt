package com.annaalfiani.gmcapps.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.extensions.gone
import com.annaalfiani.gmcapps.extensions.showToast
import com.annaalfiani.gmcapps.extensions.visible
import com.annaalfiani.gmcapps.utils.extensions.toast
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : AppCompatActivity() {

    private val signUpViewModel : SignUpViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        observer()
        register()
    }

    private fun observer() = signUpViewModel.listenToState().observer(this, Observer { handleUiState(it) })

    private fun handleUiState(it: SignUpState) {
        when(it){
            is SignUpState.Loading -> handleLoading(it.state)
            is SignUpState.ShowToast -> showToast(it.message)
            is SignUpState.Success -> handleSuccess()
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) loading.visible() else loading.gone()
    }

    private fun handleSuccess() {
        toast("berhasil register")
        finish()
    }

    private fun register(){
        btn_register.setOnClickListener {
            val name = register_fullName_editText.text.toString().trim()
            val email = register_email_editText.text.toString().trim()
            val pass = register_password_editText.text.toString().trim()
            //val telp = register_phone_editText.text.toString().trim()
            signUpViewModel.register(name, email, pass,telp)
        }
    }
}
