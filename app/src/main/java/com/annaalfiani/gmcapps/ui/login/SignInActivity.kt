package com.annaalfiani.gmcapps.ui.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.ui.main.MainActivity
import com.annaalfiani.gmcapps.utils.Utilities
import com.annaalfiani.gmcapps.utils.extensions.showInfoAlert
import com.annaalfiani.gmcapps.utils.extensions.toast
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInActivity : AppCompatActivity() {
    private val signInViewModel: SignInViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        observe()
        doLogin()
    }

    private fun observe(){
        observeState()
    }

    private fun observeState() = signInViewModel.getState().observer(this, Observer { handleState(it) })

    private fun handleState(state: SignInState){
        when(state){
            is SignInState.Loading -> isLoading(state.isLoading)
            is SignInState.Success -> handleSuccess(state.token)
            is SignInState.ShowToast -> toast(state.message)
            is SignInState.ShowAlert -> showInfoAlert(state.message)
        }
    }

    private fun isLoading(b: Boolean){
        btn_login.isEnabled = !b
    }

    private fun handleSuccess(token: String){
        Utilities.setToken(this@SignInActivity, token)
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun doLogin(){
        btn_login.setOnClickListener {
            val email = et_username.text.toString().trim()
            val password = et_password.text.toString().trim()
            if (validate(email, password)) {
                signInViewModel.login(email, password)
            }
        }
    }

    private fun resetErrorState(){
        setErrorPassword(null)
        setErrorEmail(null)
    }

    private fun validate(email: String, password: String) : Boolean {
        resetErrorState()
        if (!Utilities.isValidEmail(email)){
            setErrorEmail(resources.getString(R.string.error_email_notValid))
            return false
        }
        if (!Utilities.isValidPassword(password)){
            setErrorPassword(resources.getString(R.string.error_password_notValid))
            return false
        }
        return true
    }

    private fun setErrorEmail(err : String?) { in_username.error = err }
    private fun setErrorPassword(err : String?) { in_password.error = err }

    override fun onResume() {
        super.onResume()
        if(Utilities.getToken(this@SignInActivity) != null){
            startActivity(Intent(this@SignInActivity, MainActivity::class.java))
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
