package com.marknkamau.justjava.ui.cart

import android.app.Activity
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
import com.marknkamau.justjava.models.CartItem

import com.marknkamau.justjava.utils.bindView

class CartAdapter(private val context: Context,
                  private val cartItemList: List<CartItem>,
                  private val listener: CartAdapterListener)
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
        val item = cartItemList[position]

        holder.tvItemName.text = item.itemName
        holder.tvItemQty.text = context.getString(R.string.quantity) + ": " + item.itemQty
        holder.tvItemPrice.text = context.getString(R.string.price) + " " + item.itemPrice
        if (TextUtils.equals(item.itemCinnamon, "false"))
            holder.tvCinnamon.visibility = View.GONE
        if (TextUtils.equals(item.itemChoc, "false"))
            holder.tvChocolate.visibility = View.GONE
        if (TextUtils.equals(item.itemMarshmallow, "false"))
            holder.tvMarshmallows.visibility = View.GONE

        holder.imgEdit.setOnClickListener { showEditDialog(item) }
    }

    override fun getItemCount(): Int = cartItemList.size

    private fun showEditDialog(item: CartItem) {
        val args = Bundle()
        args.putParcelable(EditCartDialog.CART_ITEM, item)

        val editCartDialog = EditCartDialog()
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
