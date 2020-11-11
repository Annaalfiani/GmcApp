package com.annaalfiani.gmcapps.ui.detail_film

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.models.v2.Schedulle
import kotlinx.android.synthetic.main.item_date.view.*

class SchedulleAdapter(private var schedulles: MutableList<Schedulle>,
                       private var detailFilmListener: DetailFilmListener)
    : RecyclerView.Adapter<SchedulleAdapter.ViewHolder>(){

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(schedulle: Schedulle, detailFilmListener: DetailFilmListener){
            with(itemView){
                txt_date.text = schedulle.date
                setOnClickListener { detailFilmListener.clickSchedulle(schedulle) }
            }
        }
    }

    fun changeList(c : List<Schedulle>){
        schedulles.clear()
        schedulles.addAll(c)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent,false))
    }

    override fun getItemCount(): Int = schedulles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(schedulles[position], detailFilmListener)
}