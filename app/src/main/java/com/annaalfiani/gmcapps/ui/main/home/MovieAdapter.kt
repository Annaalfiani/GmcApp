package com.annaalfiani.gmcapps.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.models.Movie
import kotlinx.android.synthetic.main.list_item_movie.view.*

class MovieAdapter (private val movies: MutableList<Movie>, private val homeAdapterInterface: HomeAdapterInterface) : RecyclerView.Adapter<MovieAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_movie, parent, false))
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(movies[position])

    fun updateList(it: List<Movie>){
        movies.clear()
        movies.addAll(it)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(movie: Movie){
            with(itemView){
                movie_poster.load(movie.foto)
                movie_title.text = movie.judul
                movie_genre.text = movie.genre
                setOnClickListener {
                    homeAdapterInterface.itemClick(movie)
                }
            }
        }
    }
}