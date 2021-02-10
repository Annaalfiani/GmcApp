package com.annaalfiani.gmcapps.ui.detail_film

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.models.v2.Hours
import kotlinx.android.synthetic.main.item_hour.view.*

class HourAdapter (private var hours : MutableList<Hours>, private var detailFilmListener: DetailFilmListener)
    :RecyclerView.Adapter<HourAdapter.ViewHolder>(){

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(hour: Hours, detailFilmListener: DetailFilmListener){
            with(itemView){
                txt_hour.text = hour.hour
                setOnClickListener { detailFilmListener.clickHour(hour) }
            }
        }
    }

    fun changeList(c : List<Hours>){
        hours.clear()
        hours.addAll(c)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_hour, parent, false))
    }

    override fun getItemCount(): Int = hours.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(hours[position], detailFilmListener)

}