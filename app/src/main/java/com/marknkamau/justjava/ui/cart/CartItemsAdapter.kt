package com.marknkamau.justjava.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.models.CartItem
import com.marknkamau.justjava.databinding.ItemCartItemBinding
import com.marknkamau.justjava.utils.CurrencyFormatter

class CartItemsAdapter(
    private val context: Context,
    private val onClick: (item: CartItem) -> Unit
) : RecyclerView.Adapter<CartItemsAdapter.ViewHolder>() {

    val items = mutableListOf<CartItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(context, binding, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setItems(newItems: List<CartItem>, animated: Boolean = true) {
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

    inner class ViewHolder(
        private val context: Context,
        private val binding: ItemCartItemBinding,
        private val onClick: (CartItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {
            binding.tvCartItemTotalPrice.text =
                context.getString(R.string.price_listing, CurrencyFormatter.format(item.cartItem.totalPrice))
            binding.tvCartItemOptions.text = item.options.joinToString(", ") { it.optionName }

            binding.tvCartItemProductName.text = item.cartItem.productName
            binding.tvCartItemQuantity.text = "${item.cartItem.quantity}x"
            binding.root.setOnLongClickListener {
                onClick(item)
                false
            }
        }
    }

    class ItemDiffCallback<CartItem>(
        private val oldList: MutableList<CartItem>,
        private val newList: MutableList<CartItem>
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
