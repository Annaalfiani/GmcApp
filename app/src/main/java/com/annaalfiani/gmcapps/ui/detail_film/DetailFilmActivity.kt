package com.annaalfiani.gmcapps.ui.detail_film

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.extensions.gone
import com.annaalfiani.gmcapps.extensions.showToast
import com.annaalfiani.gmcapps.extensions.visible
import com.annaalfiani.gmcapps.models.*
import com.annaalfiani.gmcapps.models.v2.Cinema
import com.annaalfiani.gmcapps.models.v2.Hours
import com.annaalfiani.gmcapps.models.v2.Schedulle
import com.annaalfiani.gmcapps.ui.login.SignInActivity
import com.annaalfiani.gmcapps.ui.main.MainActivity
import com.annaalfiani.gmcapps.ui.select_seat.SeatActivity
import com.annaalfiani.gmcapps.utils.Utilities
import com.annaalfiani.gmcapps.utils.extensions.toast
import kotlinx.android.synthetic.main.activity_detail_film.*
import kotlinx.android.synthetic.main.content_detail_film.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFilmActivity : AppCompatActivity(), DetailFilmListener {

    private val detailFilmViewModel : DetailFilmViewModel by viewModel()
    private var restrucutureSeat : ArrayList<Seat> = arrayListOf()
    private val midtrans  = Payment()
    private lateinit var user : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_film)
        setSupportActionBar(toolbar)
        setupToolbar()
        setUpRecycler()
        setUpDetailMovie()
        observe()
        detailFilmViewModel.fetchProfile("Bearer ${Utilities.getToken(this@DetailFilmActivity)}")
    }

    private fun setupToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener { finish() }

    }

    private fun setUpRecycler() {
        setUpRecyclerViewSchedulles()
        setUpRecyclerViewStudios()
        setUpRecyclerViewHours()
    }

    private fun setUpRecyclerViewSchedulles(){
        recycler_date.apply {
            adapter = SchedulleAdapter(mutableListOf(), this@DetailFilmActivity)
            layoutManager = LinearLayoutManager(this@DetailFilmActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setUpRecyclerViewStudios(){
        recycler_studio.apply {
            adapter = StudioAdapter(mutableListOf(), this@DetailFilmActivity)
            layoutManager = LinearLayoutManager(this@DetailFilmActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setUpRecyclerViewHours(){
        recycler_hour.apply {
            adapter = HourAdapter(mutableListOf(), this@DetailFilmActivity)
            layoutManager = GridLayoutManager(this@DetailFilmActivity, 3)
        }
    }

    private fun observe(){
        oberveState()
        observeSeats()
        observeSelectedSeats()
        observeSchedulles()
        observeStudios()
        observeHours()
    }

    private fun oberveState() = detailFilmViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun observeSchedulles() = detailFilmViewModel.listenToSchedulles().observe(this, Observer { handleSchedulles(it) })
    private fun observeStudios() = detailFilmViewModel.listenToStudios().observe(this, Observer { handleStudios(it) })
    private fun observeHours() = detailFilmViewModel.listenToHours().observe(this, Observer { handleHours(it) })
    private fun observeSeats() = detailFilmViewModel.getSeats().observe(this, Observer { handleSeats(it) })
    private fun observeSelectedSeats() = detailFilmViewModel.getSelectedSeats().observe(this, Observer { handleSelectedSeats(it)})
    private fun observeUser() = detailFilmViewModel.getUser().observe(this, Observer { HandleUser(it) })

    private fun handleUiState(state: DetailFilmState?) {
        state?.let {
            when(it){
                is DetailFilmState.Loading -> handleLoading(it.state)
                is DetailFilmState.ShowToast -> showToast(it.message)
                is DetailFilmState.SuccessOrder -> {
                    toast("Berhasil order")
                    startActivity(Intent(this@DetailFilmActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) loading.visible() else loading.gone()
    }

    private fun setUpDetailMovie() {
        getPassedFilm()?.let {
            detail_movie_img.load(it.foto)
            detail_movie_title.text = it.judul
            detail_movie_genre.text = it.genre
            sinopsis.text = it.sinopsis
        }
    }

    private fun handleSchedulles(list: List<Schedulle>?) {
        list?.let {
            recycler_date.adapter?.let { adapter ->
                if (adapter is SchedulleAdapter) adapter.changeList(it)
            }
        }
    }

    private fun handleHours(list: List<Hours>?) {
        list?.let {
            recycler_hour.visible()
            recycler_hour.adapter?.let { adapter ->
                if (adapter is HourAdapter) adapter.changeList(it)
            }
        }
    }

    private fun handleStudios(list: List<Cinema>?) {
        list?.let {
            recycler_studio.adapter?.let { adapter ->
                if (adapter is StudioAdapter) adapter.changeList(it)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleSeats(k: Kursi?){
        k?.let {
            txt_empty_seat.text = "Sisa ${it.empty_seat} Kursi"
            selectSeatButtonClick(it)
        }
    }

    private fun selectSeatButtonClick(seat: Kursi) {
        btn_choose_seat.setOnClickListener {
            startActivityForResult(Intent(this@DetailFilmActivity, SeatActivity::class.java).apply {
                putExtra("seat_info", seat)
            }, 89)
        }
    }

    private fun handleSelectedSeats(it: List<Seat>){
        txt_selected_seat.text = if(it.isEmpty()) resources.getString(R.string.no_seat_selected) else it.joinToString { it.nama_kursi.toString() }
    }



    private fun fetchSchedulles() = detailFilmViewModel.fetchSchedulles(getPassedFilm()?.id.toString())
    private fun fetchStudios(date: String) = detailFilmViewModel.fetchStudios(date, getPassedFilm()?.id.toString())
    private fun fetchHours(dateId : String, studioId : String) = detailFilmViewModel.fetchHours(dateId, studioId)
    private fun fetchSeats(moviedId: String, studioId : String, date: String, hour: String) = detailFilmViewModel.fetchSeats(moviedId, studioId, date, hour)
    private fun getPassedFilm() = intent.getParcelableExtra<Movie>("MOVIE")

    private fun setSelectedDate(schedulle: Schedulle) = detailFilmViewModel.setSelectedDate(schedulle)
    private fun setSelectedCinema(cinema: Cinema) = detailFilmViewModel.setSelectedCinema(cinema)
    private fun setSelectedHour(hour: Hours) = detailFilmViewModel.setSelectedHour(hour)
    private fun getSelectedDate() = detailFilmViewModel.getSelectedDate().value
    private fun getSelectedCinema() = detailFilmViewModel.getSelectedCinema().value
    private fun getSelectedHour() = detailFilmViewModel.getSelectedHour().value

    private fun onSelectSeatsReturn(selectedSeats: List<Seat>) = detailFilmViewModel.setSelectedSeats(selectedSeats)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 89 && resultCode == Activity.RESULT_OK && data != null){
            val selectedSeats = data.getParcelableArrayListExtra<Seat>("selected_seats")
            selectedSeats?.let {
                onSelectSeatsReturn(it)
            }
            restrucutureSeat = selectedSeats!!
            btn_order.isEnabled = true
            createOrder()
        }
    }

    private fun checkIsLoggedIn() : Boolean = Utilities.getToken(this@DetailFilmActivity) != null

    private fun HandleUser(it: User) { user = it.id.toString() }

    private fun createOrder() {
        if (!checkIsLoggedIn()){
            alertNotLogin("silahkan login dahulu")
        }else{
            observeUser()
            val token = Utilities.getToken(this@DetailFilmActivity)
            val order = CreateOrder(
                id_studio = getSelectedCinema()?.id,
                id_film = getPassedFilm()?.id,
                tanggal = getSelectedCinema()?.dateId.toString(),
                jam = getSelectedHour()?.hour,
                harga = getSelectedDate()?.price,
                seats = restrucutureSeat
            )

            midtrans.initPayment(this@DetailFilmActivity, token!!, order, detailFilmViewModel)
            btn_order.setOnClickListener {
                val _harga = getSelectedDate()?.price
                val _film = getPassedFilm()?.judul
                midtrans.showPayment(this@DetailFilmActivity, user, _harga!!, restrucutureSeat.count(), _film!!)
            }
        }
    }

    private fun alertNotLogin(m : String){
        AlertDialog.Builder(this).apply {
            setMessage(m)
            setPositiveButton("login"){dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(this@DetailFilmActivity, SignInActivity::class.java))

            }
        }.show()
    }

    override fun clickSchedulle(schedulle: Schedulle) {
        fetchStudios(schedulle.date!!)
        setSelectedDate(schedulle)
        recycler_hour.gone()
    }

    override fun clickCinema(cinema: Cinema) {
        fetchHours(cinema.dateId.toString(), cinema.id.toString())
        setSelectedCinema(cinema)
    }
    override fun clickHour(hour: Hours) {
        linear_button_seat.visible()
        txt_empty_seat.visible()
        setSelectedHour(hour)
        fetchSeats(getPassedFilm()?.id.toString(), getSelectedCinema()?.id.toString(), getSelectedDate()?.date.toString(), hour.hour.toString())
    }


    override fun onResume() {
        super.onResume()
        fetchSchedulles()
    }

}