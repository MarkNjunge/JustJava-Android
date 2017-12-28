package com.marknkamau.justjavastaff.ui.main

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marknkamau.justjavastaff.R
import com.marknkamau.justjavastaff.models.Order
import kotlinx.android.synthetic.main.item_orders.view.*
import java.text.SimpleDateFormat

class OrdersAdapter(private val onClick: (Order) -> Unit) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {
    private val items by lazy { mutableListOf<Order>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_orders))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], onClick)

    override fun getItemCount() = items.size

    fun setItems(items: MutableList<Order>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(order: Order, onClick: (Order) -> Unit) {
            val dateFormat = SimpleDateFormat("hh:mm a, d MMM")

            with(itemView) {
                tvItemsCount.text = order.itemsCount.toString()
                tvOrderId.text = order.orderId
                tvTotalPrice.text = order.totalPrice.toString()
                tvTimestamp.text = dateFormat.format(order.timestamp)
                orderItem.setOnClickListener { onClick(order) }
            }
        }
    }

    private fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }
}
