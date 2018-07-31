package com.marknkamau.justjava.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.models.Order
import com.marknkamau.justjava.utils.formatForApp
import kotlinx.android.synthetic.main.item_previous_order.view.*

class PreviousOrderAdapter(private val context: Context, private val onClick: (order: Order) -> Unit) : RecyclerView.Adapter<PreviousOrderAdapter.ViewHolder>() {
    private val items by lazy { mutableListOf<Order>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_previous_order))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], context, onClick)

    override fun getItemCount() = items.size

    fun setItems(items: MutableList<Order>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(order: Order, context: Context, onClick: (order: Order) -> Unit) {
            itemView.tvTimestamp.text = order.date.formatForApp()
            itemView.tvStatus.text = order.status.name.toLowerCase().capitalize()
            itemView.tvAddress.text = order.deliveryAddress
            itemView.tvOrderInfo.text = context.resources.getQuantityString(R.plurals.order_info, order.itemsCount, order.itemsCount, order.totalPrice)

            itemView.rootLayout.setOnClickListener {
                onClick(order)
            }
        }
    }


    private fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }
}
