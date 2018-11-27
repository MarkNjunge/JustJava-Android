package com.marknkamau.justjava.ui.cart

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.marknkamau.justjava.R
import com.marknjunge.core.model.OrderItem

import kotlinx.android.synthetic.main.item_cart.view.*

class CartAdapter(private val context: Context, private val onEditClick: (OrderItem) -> Unit) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private val items by lazy { mutableListOf<OrderItem>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_cart))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], context, onEditClick)

    override fun getItemCount() = items.size

    fun setItems(items: MutableList<OrderItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: OrderItem, context: Context, onEditClick: (OrderItem) -> Unit) {
            itemView.tvItemName.text = item.itemName
            itemView.tvItemQty.text = context.getString(R.string.quantity_listing, item.itemQty)
            itemView.tvItemPrice.text = context.getString(R.string.price_listing, item.itemPrice)

            val toppings = mutableListOf<String>()

            if (item.itemCinnamon)
                toppings.add("Cinnamon")

            if (item.itemChoc)
                toppings.add("Chocolate")

            if (item.itemMarshmallow)
                toppings.add("Marshmallows")

            if (toppings.isNotEmpty()){
                itemView.tvToppings.text = toppings.toString().replace("[", "").replace("]", "")
            }else{
                itemView.tvToppings.visibility = View.GONE
            }

            itemView.setOnLongClickListener {
                onEditClick(item)
                true
            }
        }
    }

    private fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }
}
