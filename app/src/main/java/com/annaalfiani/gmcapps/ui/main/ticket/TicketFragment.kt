package com.annaalfiani.gmcapps.ui.main.ticket

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.extensions.gone
import com.annaalfiani.gmcapps.extensions.showToast
import com.annaalfiani.gmcapps.extensions.visible
import com.annaalfiani.gmcapps.models.OrderDetails
import com.annaalfiani.gmcapps.ui.login.SignInActivity
import com.annaalfiani.gmcapps.ui.main.MainActivity
import com.annaalfiani.gmcapps.ui.register.SignUpActivity
import com.annaalfiani.gmcapps.utils.Utilities
import kotlinx.android.synthetic.main.fragment_not_logged_in.view.*
import kotlinx.android.synthetic.main.fragment_ticket.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TicketFragment : Fragment(){

    private val ticketViewModel : TicketViewModel by viewModel()

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

            rv_tiket.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = TicketAdapter(mutableListOf(), requireActivity())
            }

            ticketViewModel.listenToState().observer(requireActivity(), Observer { handleUIState(it) })
            ticketViewModel.listenToOrderDetails().observe(requireActivity(), Observer { handleOrderDetails(it) })
        }else{
            setupButtonNotLoggedIn()
        }
    }

    private fun handleOrderDetails(it: List<OrderDetails>) {
        rv_tiket.adapter?.let { adapter ->
            if (adapter is TicketAdapter){
                adapter.changelist(it)
            }
        }
    }

    private fun handleUIState(it: TicketState) {
        when(it){
            is TicketState.ShowToast -> requireActivity().showToast(it.message)
            is TicketState.IsLoadinng -> {
                if (it.state){
                    loading.visible()
                }else{
                    loading.gone()
                }
            }
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

    override fun onResume() {
        super.onResume()
        ticketViewModel.fetchMyOrders("Bearer ${Utilities.getToken(requireActivity())}")
    }
}