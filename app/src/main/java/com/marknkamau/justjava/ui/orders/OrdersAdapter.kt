package com.marknkamau.justjava.ui.orders

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.marknjunge.core.data.model.Order
import com.marknjunge.core.data.model.OrderStatus
import com.marknkamau.justjava.R
import com.marknkamau.justjava.databinding.ItemOrderBinding
import com.marknkamau.justjava.utils.CurrencyFormatter
import com.marknkamau.justjava.utils.DateTime
import com.marknkamau.justjava.utils.capitalize

class OrdersAdapter(
    private val context: Context,
    private val onClick: (item: Order) -> Unit
) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    val items = mutableListOf<Order>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(context, binding, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setItems(newItems: List<Order>, animated: Boolean = true) {
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
        private val binding: ItemOrderBinding,
        private val onClick: (Order) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.tvOrderId.text = order.id
            binding.tvOrderDate.text = DateTime.fromTimestamp(order.datePlaced).format("hh:mm a, d MMM yyyy")
            binding.tvOrderStatus.text = order.status.s.capitalize().replace("_", " ")
            binding.tvOrderItems.text = order.items.joinToString { it.productName }
            binding.tvOrderTotal.text =
                context.getString(R.string.price_listing, CurrencyFormatter.format(order.totalPrice))

            val statusTextColor = when (order.status) {
                OrderStatus.PENDING -> R.color.colorOrderPendingText
                OrderStatus.CONFIRMED -> R.color.colorOrderConfirmedText
                OrderStatus.IN_PROGRESS -> R.color.colorOrderInProgressText
                OrderStatus.COMPLETED -> R.color.colorOrderCompletedText
                OrderStatus.CANCELLED -> R.color.colorOrderCancelledText
            }
            binding.tvOrderStatus.setTextColor(ContextCompat.getColor(context, statusTextColor))

            val statusDrawable = when (order.status) {
                OrderStatus.PENDING -> R.drawable.bg_order_pending
                OrderStatus.CONFIRMED -> R.drawable.bg_order_confirmed
                OrderStatus.IN_PROGRESS -> R.drawable.bg_order_in_progress
                OrderStatus.COMPLETED -> R.drawable.bg_order_completed
                OrderStatus.CANCELLED -> R.drawable.bg_order_cancelled
            }
            binding.tvOrderStatus.setBackgroundResource(statusDrawable)
            binding.root.setOnClickListener {
                onClick(order)
            }
        }
    }

    class ItemDiffCallback<Order>(
        private val oldList: MutableList<Order>,
        private val newList: MutableList<Order>
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
