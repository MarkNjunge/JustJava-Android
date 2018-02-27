package com.marknkamau.justjavastaff.ui.orderdetails

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
import com.marknkamau.justjavastaff.models.OrderItem
import kotlinx.android.synthetic.main.item_order_item.view.*
import timber.log.Timber

class OrderItemsAdapter() : RecyclerView.Adapter<OrderItemsAdapter.ViewHolder>() {
    private val items by lazy { mutableListOf<OrderItem>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_order_item))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    fun setItems(items: MutableList<OrderItem>){
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(element: OrderItem) {
            itemView.run {
                Timber.i("Bingind")
                tvItemName.text = element.itemName
                tvItemQuantity.text = element.itemQty.toString()
                tvChocolate.visibility = if(element.itemChoc) View.VISIBLE else View.GONE
                tvCinnamon.visibility = if(element.itemCinnamon) View.VISIBLE else View.GONE
                tvMarshmallows.visibility = if(element.itemMarshmallow) View.VISIBLE else View.GONE
            }
        }
    }

    private fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }
}