package com.marknkamau.justjava.ui.cart

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.CartDao
import com.marknkamau.justjava.models.CartItem
import com.marknkamau.justjava.models.CartItemRoom

import com.marknkamau.justjava.utils.bindView

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
        val item = cartItemList?.get(position)

        holder.tvItemName.text = item?.itemName
        holder.tvItemQty.text = context.getString(R.string.quantity) + ": " + item?.itemQty
        holder.tvItemPrice.text = context.getString(R.string.price) + " " + item?.itemPrice

        if (!(item?.itemCinnamon as Boolean))
            holder.tvCinnamon.visibility = View.GONE

        if (!item.itemChoc)
            holder.tvChocolate.visibility = View.GONE

        if (!item.itemMarshmallow)
            holder.tvMarshmallows.visibility = View.GONE

        holder.imgEdit.setOnClickListener { showEditDialog(item) }
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
        val tvItemName: TextView by bindView(R.id.tv_item_name)
        val tvItemPrice: TextView by bindView(R.id.tv_item_price)
        val tvItemQty: TextView by bindView(R.id.tv_item_qty)
        val tvCinnamon: TextView by bindView(R.id.tv_cinnamon)
        val tvChocolate: TextView by bindView(R.id.tv_chocolate)
        val tvMarshmallows: TextView by bindView(R.id.tv_marshmallows)
        val imgEdit: ImageView by bindView(R.id.img_edit)
    }
}
