package com.marknkamau.justjava.ui.orderDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.marknjunge.core.data.model.OrderItem
import com.marknkamau.justjava.R
import com.marknkamau.justjava.databinding.ItemOrderItemBinding
import com.marknkamau.justjava.utils.CurrencyFormatter

class OrderItemsAdapter(
    private val context: Context
) : RecyclerView.Adapter<OrderItemsAdapter.ViewHolder>() {

    val items = mutableListOf<OrderItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(context, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setItems(newItems: List<OrderItem>, animated: Boolean = true) {
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
        private val binding: ItemOrderItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderItem) {
            binding.tvOrderDetailProductName.text = item.productName
            binding.tvOrderDetailQuantity.text = "${item.quantity}x"
            binding.tvOrderDetailPrice.text = context.getString(R.string.price_listing, CurrencyFormatter.format(item.totalPrice))
            binding.tvOrderDetailOptions.text = item.options.joinToString(", ") { it.optionName }
        }
    }

    class ItemDiffCallback<OrderItem>(
        private val oldList: MutableList<OrderItem>,
        private val newList: MutableList<OrderItem>
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