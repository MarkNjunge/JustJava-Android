package com.marknkamau.justjavastaff.ui.main

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marknkamau.justjavastaff.R
import com.marknkamau.justjavastaff.models.Order
import com.marknkamau.justjavastaff.models.OrderStatus
import kotlinx.android.synthetic.main.item_orders.view.*
import java.text.SimpleDateFormat

class OrdersAdapter(private val context: Context, private val onClick: (Order) -> Unit) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {
    private val items by lazy { mutableListOf<Order>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_orders))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(context, items[position], onClick)

    override fun getItemCount() = items.size

    fun setItems(items: MutableList<Order>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, order: Order, onClick: (Order) -> Unit) {
            val dateFormat = SimpleDateFormat("hh:mm a, d MMM")

            with(itemView) {
                tvItemsCount.text = order.itemsCount.toString()
                tvOrderId.text = order.orderId
                tvTotalPrice.text = order.totalPrice.toString()
                tvTimestamp.text = dateFormat.format(order.timestamp)
                val color = when (order.status) {
                    OrderStatus.PENDING.name -> ContextCompat.getColor(context, R.color.colorPending)
                    OrderStatus.INPROGRESS.name -> ContextCompat.getColor(context, R.color.colorInProgress)
                    OrderStatus.COMPLETED.name -> ContextCompat.getColor(context, R.color.colorCompleted)
                    OrderStatus.DELIVERED.name -> ContextCompat.getColor(context, R.color.colorDelivered)
                    OrderStatus.CANCELLED.name -> ContextCompat.getColor(context, R.color.colorCancelled)
                    else -> ContextCompat.getColor(context, R.color.colorPending)
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
