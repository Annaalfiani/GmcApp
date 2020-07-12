package com.annaalfiani.gmcapps.ui.detail_movie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.models.Movie
import com.annaalfiani.gmcapps.models.MovieSchedule
import com.annaalfiani.gmcapps.ui.order.OrderActivity
import com.annaalfiani.gmcapps.utils.Utilities
import com.annaalfiani.gmcapps.utils.extensions.gone
import com.annaalfiani.gmcapps.utils.extensions.showInfoAlert
import com.annaalfiani.gmcapps.utils.extensions.toast
import com.annaalfiani.gmcapps.utils.extensions.visible
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail_movie.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity(), DetailMovieInterface {
    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        setupToolbar()
        setupScheduleRecycler()
        observe()
        fetchMovie()
        fetchSchedule()
    }

    private fun setupToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener { finish() }

    }

    private fun setupScheduleRecycler(){
        detail_schedule_recyclerView.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = SectionedRecyclerViewAdapter()
        }
    }

    private fun fetchMovie() = detailViewModel.fetchMovieDetail(getPassedMovie()?.id.toString())
    private fun fetchSchedule() = detailViewModel.fetchMovieSchedule(getPassedMovie()?.id.toString())

    private fun getPassedMovie() : Movie? = intent.getParcelableExtra("FILM")

    private fun observe(){
        observeState()
        observeMovie()
        observeSchedules()
    }

    private fun observeSchedules() = detailViewModel.getSchedules().observe(this, Observer { handleSchedules(it) })
    private fun observeState() = detailViewModel.getState().observer(this, Observer { handleState(it) })
    private fun observeMovie() = detailViewModel.getMovie().observe(this, Observer { handleMovie(it) })

    private fun handleMovie(movie: Movie){
        fill(movie)
    }

    private fun handleSchedules(schedules: HashMap<String, List<MovieSchedule>>?){
        schedules?.let {
            val sectionedAdapter = SectionedRecyclerViewAdapter()
            schedules.forEach { (t, u) -> sectionedAdapter.addSection(SectionScheduleAdapter(t, u, this@DetailActivity)) }
            detail_schedule_recyclerView.apply {
                adapter = sectionedAdapter
            }
        }
    }

    private fun fill(movie: Movie){
        detail_movie_img.load("https://www.greenscene.co.id/wp-content/uploads/2020/04/Money-Heist-1.jpg")
        detail_movie_title.text = movie.judul
        detail_movie_genre.text = movie.genre
        sinopsis.text = movie.sinopsis
    }

    private fun handleState(state: DetailState){
        when(state){
            is DetailState.Loading -> isLoading(state.isLoading)
            is DetailState.ShowToast -> toast(state.message)
        }
    }

    private fun isLoading(b: Boolean){
        if(b){
            loading.visible()
        }else{
            loading.gone()
        }
    }

    override fun itemClick(movie: MovieSchedule) {
        Utilities.getToken(this)?.let {
            startActivity(Intent(this@DetailActivity, OrderActivity::class.java).apply {
                putExtra("SCHEDULE", movie)
                putExtra("FILM", getPassedMovie())
            })
        } ?: kotlin.run {
            showInfoAlert(resources.getString(R.string.please_login))
        }
    }
}
