package com.annaalfiani.gmcapps.ui.detail_film

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
//                val colorGray = ContextCompat.getColor(context, R.color.gray_primary)
//                bg_item.setBackgroundColor(colorGray)
                txt_date.text = schedulle.date
                txt_day.text = schedulle.day
                setOnClickListener {
//                    val colorBlue = ContextCompat.getColor(context, R.color.text_light_blue)
//                    bg_item.setBackgroundColor(colorBlue)
                    detailFilmListener.clickSchedulle(schedulle)
                }
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