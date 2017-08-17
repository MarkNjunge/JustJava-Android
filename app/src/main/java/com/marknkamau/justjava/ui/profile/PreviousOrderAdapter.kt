package com.marknkamau.justjava.ui.profile

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.marknkamau.justjava.R
import com.marknkamau.justjava.models.PreviousOrder
import com.marknkamau.justjava.utils.bindView

import java.text.DateFormat
import java.util.Calendar

class PreviousOrderAdapter internal constructor(private val mContext: Context, private val previousOrders: List<PreviousOrder>) : RecyclerView.Adapter<PreviousOrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val itemView = inflater.inflate(R.layout.item_previous_order, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = previousOrders[position]

        holder.tvTimestamp.text = getFormattedDate(java.lang.Long.valueOf(order.timestamp)!!)
        holder.tvTotalPrice.text = String.format("%s%S", mContext.getString(R.string.ksh), order.totalPrice)
        holder.tvOrderStatus.text = order.orderStatus
        holder.tvDeliveryAddress.text = order.deliveryAddress
    }

    private fun getFormattedDate(timestamp: Long): String {
        try {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp
            val currentTime = calendar.time
            return DateFormat.getDateTimeInstance().format(currentTime)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return timestamp.toString()
    }

    override fun getItemCount(): Int =  previousOrders.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTimestamp: TextView by bindView(R.id.tv_timestamp)
        val tvTotalPrice: TextView by bindView(R.id.tv_total_price)
        val tvDeliveryAddress: TextView by bindView(R.id.tv_delivery_address)
        val tvOrderStatus: TextView by bindView(R.id.tv_order_status)
    }
}
