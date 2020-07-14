package com.annaalfiani.gmcapps.ui.main.ticket

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.models.OrderDetails
import com.annaalfiani.gmcapps.ui.detail_ticket.DetailTicketActivity
import kotlinx.android.synthetic.main.list_item_ticket.view.*


class TicketAdapter (private var orderDetails: MutableList<OrderDetails>, private var context: Context)
    : RecyclerView.Adapter<TicketAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_ticket, parent, false))
    }

    override fun getItemCount(): Int = orderDetails.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(orderDetails[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(orderDetails: OrderDetails, context: Context){
            with(itemView){
                film_name.text = orderDetails.order.film.judul
                schedule.text = "${orderDetails.order.tanggal} ${orderDetails.order.jam}"
                studio_name.text = orderDetails.order.studio.nama
                setOnClickListener {
                    context.startActivity(Intent(context, DetailTicketActivity::class.java).apply {
                        putExtra("ORDER_DETAILS", orderDetails)
                    })
                }
            }
        }
    }

    fun changelist(c : List<OrderDetails>){
        orderDetails.clear()
        orderDetails.addAll(c)
        notifyDataSetChanged()
    }

}