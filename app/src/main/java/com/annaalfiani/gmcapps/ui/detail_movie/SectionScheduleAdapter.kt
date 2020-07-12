package com.annaalfiani.gmcapps.ui.detail_movie

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.models.MovieSchedule
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import kotlinx.android.synthetic.main.list_item_movie_schedule.view.*
import kotlinx.android.synthetic.main.list_item_schedule_header.view.*

internal class SectionScheduleAdapter(private val headerTitle: String, aSchedules: List<MovieSchedule>, private val detailMovieInterface: DetailMovieInterface) :

    Section(SectionParameters.builder().itemResourceId(R.layout.list_item_movie_schedule).headerResourceId(R.layout.list_item_schedule_header).build()){

    private var schedules: List<MovieSchedule> = aSchedules

    override fun getContentItemsTotal() = schedules.size

    override fun getItemViewHolder(view: View) = ScheduleViewHolder(view)

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) = (holder as ScheduleViewHolder).bind(schedules[position], detailMovieInterface)

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder) = (holder as HeaderItemViewHolder).bind(headerTitle)

    override fun getHeaderViewHolder(view: View) = HeaderItemViewHolder(view)

}


class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun bind(schedule: MovieSchedule, detailMovieInterface: DetailMovieInterface){
        with(itemView){
            schedule_studio.text = schedule.studio?.nama
            schedule_hour.text = schedule.hour
            setOnClickListener {
                detailMovieInterface.itemClick(schedule)
            }
        }
    }
}

class HeaderItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun bind(title: String){
        itemView.header_schedule_title.text = title
    }
}