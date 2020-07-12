package com.annaalfiani.gmcapps.ui.main.ticket

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.ui.login.SignInActivity
import com.annaalfiani.gmcapps.ui.main.MainActivity
import com.annaalfiani.gmcapps.ui.register.SignUpActivity
import com.annaalfiani.gmcapps.utils.Utilities
import kotlinx.android.synthetic.main.fragment_not_logged_in.view.*

class TicketFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if(checkIsLoggedIn()){
            inflater.inflate(R.layout.fragment_ticket, container, false)
        }else{
            inflater.inflate(R.layout.fragment_not_logged_in, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (checkIsLoggedIn()){

        }else{
            setupButtonNotLoggedIn()
        }
    }

    private fun setupButtonNotLoggedIn(){
        requireView().btn_login.setOnClickListener {
            startActivityForResult(Intent(requireActivity(), SignInActivity::class.java), 1)
        }
        requireView().btn_register.setOnClickListener {
            startActivityForResult(Intent(requireActivity(), SignUpActivity::class.java), 1)
        }
    }

    private fun checkIsLoggedIn() : Boolean = Utilities.getToken(requireActivity()) != null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }
    }
}