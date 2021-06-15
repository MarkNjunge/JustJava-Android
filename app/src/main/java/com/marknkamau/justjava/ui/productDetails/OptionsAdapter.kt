package com.marknkamau.justjava.ui.productDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.models.AppProductChoiceOption
import com.marknkamau.justjava.databinding.ItemProductChoiceOptionBinding
import com.marknkamau.justjava.utils.CurrencyFormatter

class OptionsAdapter(private val context: Context) : RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {

    private val items by lazy { mutableListOf<AppProductChoiceOption>() }
    var onSelected: ((option: AppProductChoiceOption, checked: Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductChoiceOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], context)
    }

    fun setItems(newItems: List<AppProductChoiceOption>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemProductChoiceOptionBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(option: AppProductChoiceOption, context: Context) {
            val formattedPrice = CurrencyFormatter.format(option.price)
            binding.tvOptionPrice.text = context.getString(R.string.price_listing_w_add, formattedPrice)
            binding.cbOptionTitle.text = option.name
            binding.cbOptionTitle.isChecked = option.isChecked

            binding.cbOptionTitle.setOnClickListener {
                onSelected?.invoke(option, binding.cbOptionTitle.isChecked)
            }
        }
    }
}
