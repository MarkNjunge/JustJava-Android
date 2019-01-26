package com.marknkamau.justjavastaff.ui.orders

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marknjunge.core.model.Order
import com.marknjunge.core.model.OrderStatus
import com.marknkamau.justjavastaff.R

import kotlinx.android.synthetic.main.item_orders.view.*
import java.text.SimpleDateFormat

class OrdersAdapter(private val context: Context, private val onClick: (Order) -> Unit) : androidx.recyclerview.widget.RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {
    private val items by lazy { mutableListOf<Order>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_orders))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(context, items[position], onClick)

    override fun getItemCount() = items.size

    fun setItems(items: MutableList<Order>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, order: Order, onClick: (Order) -> Unit) {
            val dateFormat = SimpleDateFormat("hh:mm a, d MMM")

            with(itemView) {
                tvItemsCount.text = order.itemsCount.toString()
                tvOrderId.text = order.orderId
                tvTimestamp.text = dateFormat.format(order.date)
                val color = when (order.status) {
                    OrderStatus.PENDING -> ContextCompat.getColor(context, R.color.colorPending)
                    OrderStatus.INPROGRESS -> ContextCompat.getColor(context, R.color.colorInProgress)
                    OrderStatus.COMPLETED -> ContextCompat.getColor(context, R.color.colorCompleted)
                    OrderStatus.DELIVERED -> ContextCompat.getColor(context, R.color.colorDelivered)
                    OrderStatus.CANCELLED -> ContextCompat.getColor(context, R.color.colorCancelled)
                }

                viewStatus.setBackgroundColor(color)
                orderItem.setOnClickListener { onClick(order) }
            }
        }
    }

    private fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }
}
