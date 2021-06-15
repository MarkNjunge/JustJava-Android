package com.marknkamau.justjava.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.marknjunge.core.data.model.Product
import com.marknkamau.justjava.R
import com.marknkamau.justjava.databinding.ItemProductBinding
import com.marknkamau.justjava.utils.CurrencyFormatter
import com.squareup.picasso.Picasso

class ProductsAdapter(
    private val onclick: (product: Product, View) -> Unit
) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    private val items: MutableList<Product> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], onclick)
    }

    override fun getItemCount() = items.size

    fun setItems(newItems: List<Product>, animated: Boolean = true) {
        if (animated) {
            val result = DiffUtil.calculateDiff(ItemDiffCallback(items, newItems.toMutableList()))

            items.clear()
            items.addAll(newItems)
            result.dispatchUpdatesTo(this)
        } else {
            items.clear()
            items.addAll(newItems)
            notifyDataSetChanged()
        }
    }

    class ViewHolder(
        private val context: Context,
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product, onclick: (Product, View) -> Unit) {
            val price = CurrencyFormatter.format(product.price)

            binding.tvProductName.text = product.name
            binding.tvProductPrice.text = context.resources.getString(R.string.price_listing, price)
            Picasso.get()
                .load(product.image)
                .placeholder(R.drawable.plain_brown)
                .into(binding.imgProductImage)

            binding.root.setOnClickListener {
                onclick(product, binding.imgProductImage)
            }
        }
    }

    class ItemDiffCallback<Product>(
        private val oldList: MutableList<Product>,
        private val newList: MutableList<Product>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return newList[newItemPosition]!! == oldList[oldItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return newList[newItemPosition]!! == oldList[oldItemPosition]
        }
    }

}