package com.annaalfiani.gmcapps.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.models.Movie
import com.annaalfiani.gmcapps.ui.detail_movie.DetailActivity
import com.annaalfiani.gmcapps.utils.extensions.gone
import com.annaalfiani.gmcapps.utils.extensions.toast
import com.annaalfiani.gmcapps.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment(R.layout.fragment_home), HomeAdapterInterface {
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observe()
        fetchComingSoonMovies()
        fetchNowPlayingMovies()
    }

    private fun fetchComingSoonMovies() = homeViewModel.fetchComingSoonMovies()
    private fun fetchNowPlayingMovies() = homeViewModel.fetchNowPlayingMovies()

    private fun observe(){
        observeState()
        observeComingSoonMovies()
        observeNowPlayingMovies()
    }

    private fun observeState() = homeViewModel.getState().observer(viewLifecycleOwner, Observer { handleState(it) })
    private fun observeComingSoonMovies() = homeViewModel.getComingSoonMovies().observe(viewLifecycleOwner, Observer { handleComingSoonMovies(it) })
    private fun observeNowPlayingMovies() = homeViewModel.getNowPlayingMovies().observe(viewLifecycleOwner, Observer { handleNowPlayingMovies(it) })

    private fun handleState(state: HomeState){
        when(state){
            is HomeState.ShowToast -> requireActivity().toast(state.message)
            is HomeState.LoadingNowPlaying -> isLoadingNowPlaying(state.isLoading)
            is HomeState.LoadingComingSoon -> isLoadingComingSoon(state.isLoading)
        }
    }

    private fun isLoadingNowPlaying(b: Boolean){
        with(requireView()){
            if(b){
                loading_now_playing.visible()
            }else{
                loading_now_playing.gone()
            }
        }
    }

    private fun isLoadingComingSoon(b: Boolean){
        with(requireView()){
            if(b){
                loading_coming_soon.visible()
            }else{
                loading_coming_soon.gone()
            }
        }
    }

    private fun handleComingSoonMovies(it : List<Movie>){
        requireView().rv_comingsoon.adapter?.let { adapter -> if(adapter is MovieAdapter){
            adapter.updateList(it)
        }}
    }

    private fun handleNowPlayingMovies(it : List<Movie>){
        requireView().rv_nowplaying.adapter?.let { adapter -> if(adapter is MovieAdapter){
            adapter.updateList(it)
        }}
    }

    private fun setupUI(){
        with(requireView()){
            rv_nowplaying.apply {
                adapter = MovieAdapter(mutableListOf(), this@HomeFragment)
            }
            rv_comingsoon.apply {
                adapter = MovieAdapter(mutableListOf(), this@HomeFragment)
            }
        }
    }

    override fun itemClick(movie: Movie) {
        requireActivity().startActivity(Intent(requireActivity(), DetailActivity::class.java).apply {
            putExtra("FILM", movie)
        })
    }
}