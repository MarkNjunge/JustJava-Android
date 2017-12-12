package com.marknkamau.justjava.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marknkamau.justjava.R
import com.marknkamau.justjava.models.PreviousOrder
import kotlinx.android.synthetic.main.item_previous_order.view.*
import java.text.DateFormat
import java.util.*

class PreviousOrderAdapter(private val context: Context) : RecyclerView.Adapter<PreviousOrderAdapter.ViewHolder>() {
    private val items by lazy { mutableListOf<PreviousOrder>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_previous_order))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], context)

    override fun getItemCount() = items.size

    fun setItems(items: MutableList<PreviousOrder>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(order: PreviousOrder, context: Context) {
            itemView.tvTimestamp.text = getFormattedDate(order.timestamp.toLong())
            itemView.tvTotalPrice.text = "${context.getString(R.string.ksh)}${order.totalPrice}"
            itemView.tvOrderStatus.text = order.orderStatus
            itemView.tvDeliveryAddress.text = order.deliveryAddress
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
    }


    private fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }
}
