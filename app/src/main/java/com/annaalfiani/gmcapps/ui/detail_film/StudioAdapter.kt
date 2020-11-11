package com.annaalfiani.gmcapps.ui.detail_film

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.models.v2.Cinema
import kotlinx.android.synthetic.main.item_studio.view.*

class StudioAdapter (private var cinemas : MutableList<Cinema>, private var detailFilmListener: DetailFilmListener)
    : RecyclerView.Adapter<StudioAdapter.ViewHolder>(){

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(cinema: Cinema, detailFilmListener: DetailFilmListener){
            with(itemView){
                txt_studio.text = cinema.nama
                setOnClickListener { detailFilmListener.clickCinema(cinema) }
            }
        }
    }

    fun changeList(c : List<Cinema>){
        cinemas.clear()
        cinemas.addAll(c)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_studio, parent, false))
    }

    override fun getItemCount(): Int = cinemas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(cinemas[position], detailFilmListener)
}