package com.annaalfiani.gmcapps.ui.detail_film

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.extensions.gone
import com.annaalfiani.gmcapps.extensions.showToast
import com.annaalfiani.gmcapps.extensions.visible
import com.annaalfiani.gmcapps.models.Movie
import com.annaalfiani.gmcapps.models.v2.Cinema
import com.annaalfiani.gmcapps.models.v2.Hours
import com.annaalfiani.gmcapps.models.v2.Schedulle

import kotlinx.android.synthetic.main.activity_detail_film.*
import kotlinx.android.synthetic.main.content_detail_film.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFilmActivity : AppCompatActivity(), DetailFilmListener {

    private val detailFilmViewModel : DetailFilmViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_film)
        setSupportActionBar(toolbar)
        setupToolbar()
        setUpRecycler()
        observe()
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
        observeSchedulles()
        observeStudios()
        observeHours()
    }

    private fun oberveState() = detailFilmViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun observeSchedulles() = detailFilmViewModel.listenToSchedulles().observe(this, Observer { handleSchedulles(it) })
    private fun observeStudios() = detailFilmViewModel.listenToStudios().observe(this, Observer { handleStudios(it) })
    private fun observeHours() = detailFilmViewModel.listenToHours().observe(this, Observer { handleHours(it) })

    private fun handleHours(list: List<Hours>?) {
        list?.let {
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

    private fun handleSchedulles(list: List<Schedulle>?) {
        list?.let {
            recycler_date.adapter?.let { adapter ->
                if (adapter is SchedulleAdapter) adapter.changeList(it)
            }
        }
    }

    private fun handleUiState(state: DetailFilmState?) {
        state?.let {
            when(it){
                is DetailFilmState.Loading -> handleLoading(it.state)
                is DetailFilmState.ShowToast -> showToast(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) loading.visible() else loading.gone()
    }

    private fun fetchSchedulles() = detailFilmViewModel.fetchSchedulles(getPassedFilm()?.id.toString())
    private fun fetchStudios(date: String) = detailFilmViewModel.fetchStudios(date)
    private fun setDateId(id : String) = detailFilmViewModel.setDateId(id)
    private fun getDateId() = detailFilmViewModel.getDateId().value
    private fun fetchHours(studioId : String) = detailFilmViewModel.fetchHours(getDateId()!!, studioId)

    private fun getPassedFilm() = intent.getParcelableExtra<Movie>("MOVIE")

    override fun clickSchedulle(schedulle: Schedulle) {
        println("film id ${getPassedFilm()?.id}")
        fetchStudios(schedulle.date!!)
        println("tanggal id ${schedulle.id}")
        setDateId(schedulle.id.toString())
    }

    override fun clickCinema(cinema: Cinema) {
        //println(getDateId())
        fetchHours(cinema.id.toString())
    }

    override fun onResume() {
        super.onResume()
        fetchSchedulles()
    }

}
