package com.annaalfiani.gmcapps.ui.order

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import coil.api.load
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.models.*
import com.annaalfiani.gmcapps.ui.main.MainActivity
import com.annaalfiani.gmcapps.ui.select_seat.SeatActivity
import com.annaalfiani.gmcapps.utils.Utilities
import com.annaalfiani.gmcapps.utils.extensions.showInfoAlert
import com.annaalfiani.gmcapps.utils.extensions.toast
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.content_order.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderActivity : AppCompatActivity() {
    private val orderViewModel : OrderViewModel by viewModel()
    private var restrucutureSeat : ArrayList<Seat> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        setSupportActionBar(toolbar)
        setupToolbar()
        fill()
        observe()
        fetchSeats()
        createOrder()
    }

    private fun setupToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun fetchSeats(){
        Utilities.getToken(this)?.let { orderViewModel.fetchSeats(it,
            getPassedSchedule()?.date.toString(),
            getPassedSchedule()?.hour.toString(),
            getPassedSchedule()?.studio?.id.toString()
        ) }
    }

    private fun observe(){
        observeState()
        observeSeats()
        observeSelectedSeats()
    }

    private fun observeSeats() = orderViewModel.getSeats().observe(this, Observer { handleSeats(it) })
    private fun observeState() = orderViewModel.getState().observer(this, Observer { handleState(it) })
    private fun observeSelectedSeats() = orderViewModel.getSelectedSeats().observe(this, Observer { handleSelectedSeats(it)})

    private fun handleState(it: OrderState){
        when(it){
            is OrderState.Loading -> isLoading(it.isLoading)
            is OrderState.ShowToast -> toast(it.message)
            is OrderState.Alert -> showInfoAlert(it.message)
            is OrderState.SuccessOrder -> {
                toast("Berhasil order")
                startActivity(Intent(this@OrderActivity, MainActivity::class.java))
            }
        }
    }

    private fun isLoading(b: Boolean){
        //btn_order.isEnabled = !b
        order_button_select_seat.isEnabled = !b
    }


    private fun handleSeats(k: Kursi?){
        k?.let {
            selectSeatButtonClick(it)
        }
    }

    private fun selectSeatButtonClick(kursi: Kursi){
        order_button_select_seat.setOnClickListener {
            startActivityForResult(Intent(this@OrderActivity, SeatActivity::class.java).apply {
                putExtra("seat_info", kursi)
            }, 89)
        }
    }

    private fun handleSelectedSeats(it: List<Seat>){
        order_selected_seats.text = if(it.isEmpty()) resources.getString(R.string.no_seat_selected) else it.joinToString { it.nama_kursi.toString() }
    }

    override fun onStart() {
        super.onStart()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun fill(){
        getPassedMovie()?.let {
            order_movie_poster.load("https://www.greenscene.co.id/wp-content/uploads/2020/04/Money-Heist-1.jpg")
            order_movieName_textView.text = it.judul
            order_date_textView.text = getPassedSchedule()?.date.toString()
            order_hour_textView.text = getPassedSchedule()?.hour.toString()
            order_studio_textView.text = getPassedSchedule()?.studio?.nama.toString()
        }
    }

    private fun createOrder(){
        btn_order.setOnClickListener {
            val token = Utilities.getToken(this@OrderActivity)
            val order = CreateOrder(
                id_studio = getPassedSchedule()?.studio!!.id,
                id_film = getPassedMovie()?.id,
                id_jadwal_tayang = getPassedSchedule()?.id,
                tanggal = getPassedSchedule()?.date,
                jam = getPassedSchedule()?.hour,
                harga = getPassedSchedule()?.price,
                seats = restrucutureSeat
            )
            orderViewModel.createOrder(token!!, order)
        }
    }

    private fun getPassedSchedule() = intent.getParcelableExtra<MovieSchedule>("SCHEDULE")
    private fun getPassedMovie() = intent.getParcelableExtra<Movie>("FILM")

    private fun onSelectSeatsReturn(selectedSeats: List<Seat>) = orderViewModel.setSelectedSeats(selectedSeats)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 89 && resultCode == Activity.RESULT_OK && data != null){
            val selectedSeats = data.getParcelableArrayListExtra<Seat>("selected_seats")
//            selectedSeats?.let {
//                onSelectSeatsReturn(it as List<Seat>)
//            }
            selectedSeats?.let {
                onSelectSeatsReturn(it)
            }
            restrucutureSeat = selectedSeats!!
            btn_order.isEnabled = true
        }
    }
}