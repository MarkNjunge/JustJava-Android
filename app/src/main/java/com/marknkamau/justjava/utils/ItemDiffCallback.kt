package com.marknkamau.justjava.utils

import androidx.recyclerview.widget.DiffUtil

class ItemDiffCallback<T>(
    private val oldList: MutableList<T>,
    private val newList: MutableList<T>
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
