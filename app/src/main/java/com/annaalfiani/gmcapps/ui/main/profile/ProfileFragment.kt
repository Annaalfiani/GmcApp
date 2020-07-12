package com.annaalfiani.gmcapps.ui.main.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.utils.extensions.gone
import com.annaalfiani.gmcapps.utils.extensions.visible
import com.annaalfiani.gmcapps.models.User
import com.annaalfiani.gmcapps.ui.login.SignInActivity
import com.annaalfiani.gmcapps.ui.main.MainActivity
import com.annaalfiani.gmcapps.utils.Utilities
import com.annaalfiani.gmcapps.utils.extensions.toast
import kotlinx.android.synthetic.main.fragment_not_logged_in.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(){
    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if(checkIsLoggedIn()){
            inflater.inflate(R.layout.fragment_profile, container, false)
        }else{
            inflater.inflate(R.layout.fragment_not_logged_in, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(checkIsLoggedIn()){
            observe()
        }else{
            setupNotLoggedInView()
        }
    }

    private fun setupNotLoggedInView(){
        requireView().btn_login.setOnClickListener {
            startActivityForResult(Intent(requireActivity(), SignInActivity::class.java), 1)
        }
        requireView().btn_register.setOnClickListener {
            startActivityForResult(Intent(requireActivity(), SignInActivity::class.java), 2)
        }
    }

    private fun checkIsLoggedIn() = Utilities.getToken(requireActivity()) != null

    private fun observe(){
        observeState()
        observeUser()
    }

    private fun observeState() = profileViewModel.getState().observer(viewLifecycleOwner, Observer { handleState(it) })
    private fun observeUser() = profileViewModel.getCurrentUser().observe(viewLifecycleOwner, Observer { handleUser(it) })

    private fun handleState(state: ProfileState){
        when(state){
            is ProfileState.Loading -> isLoading(state.isLoading)
            is ProfileState.ShowToast -> requireActivity().toast(state.message)
        }
    }

    private fun isLoading(b: Boolean){
        requireView().pb_profile.isIndeterminate = b
        if(b){
            requireView().pb_profile.visible()
        }else{
            requireView().pb_profile.gone()
        }
    }

    private fun handleUser(it : User){
        requireView().tv_nama.text = it.name
        requireView().tv_email.text = it.email
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            with(requireActivity()){
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                finish()
            }
        }
    }
}