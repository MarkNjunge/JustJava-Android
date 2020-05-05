package com.marknkamau.justjava.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class BaseRecyclerViewAdapter<T>(
    @LayoutRes
    private val layoutRes: Int,
    private val bind: View.(item: T) -> Unit
) : RecyclerView.Adapter<BaseRecyclerViewAdapter<T>.ViewHolder>() {
    private val _items by lazy { mutableListOf<T>() }
    val items: List<T>
        get() = _items.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(layoutRes))

    override fun getItemCount() = _items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(_items[position])
    }

    fun setItems(newItems: List<T>, animated: Boolean = true) {
        if (animated) {
            val result = DiffUtil.calculateDiff(ItemDiffCallback(_items, newItems.toMutableList()))

            _items.clear()
            _items.addAll(newItems)
            result.dispatchUpdatesTo(this)
        } else {
            _items.clear()
            _items.addAll(newItems)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: T) = bind(itemView, item)
    }

    private fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }
}
