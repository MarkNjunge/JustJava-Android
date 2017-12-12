package com.marknkamau.justjava.ui.cart

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.CartDao
import com.marknkamau.justjava.models.CartItemRoom

import kotlinx.android.synthetic.main.item_cart.view.*

class CartAdapter(private val context: Context,
                  private val cartItemList: List<CartItemRoom>?,
                  private val listener: CartAdapterListener,
                  private val cartDao: CartDao)

    : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    interface CartAdapterListener {
        fun updateList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.item_cart, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cartItemList?.let {
            holder.bind(it[position])
        }
    }

    override fun getItemCount(): Int = cartItemList?.size ?: 0

    private fun showEditDialog(item: CartItemRoom) {
        val args = Bundle()
        args.putParcelable(EditCartDialog.CART_ITEM, item)

        val editCartDialog = EditCartDialog(cartDao)
        editCartDialog.arguments = args
        editCartDialog.show((context as AppCompatActivity).supportFragmentManager, "IMAGE_FRAGMENT")

        editCartDialog.setResponseListener(object : CartAdapterListener {
            override fun updateList() {
                listener.updateList()
            }
        })
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(item: CartItemRoom){
            itemView.tvItemName.text = item.itemName
            itemView.tvItemQty.text = context.getString(R.string.quantity) + ": " + item.itemQty
            itemView.tvItemPrice.text = context.getString(R.string.price) + " " + item.itemPrice

            if (!(item.itemCinnamon as Boolean))
                itemView.tvCinnamon.visibility = View.GONE

            if (!item.itemChoc)
                itemView.tvChocolate.visibility = View.GONE

            if (!item.itemMarshmallow)
                itemView.tvMarshmallows.visibility = View.GONE

            itemView.imgEdit.setOnClickListener { showEditDialog(item) }
        }
    }
}
