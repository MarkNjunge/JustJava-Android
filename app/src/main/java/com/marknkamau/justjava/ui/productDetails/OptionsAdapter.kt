package com.marknkamau.justjava.ui.productDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.models.AppProductChoiceOption
import com.marknkamau.justjava.utils.CurrencyFormatter
import kotlinx.android.synthetic.main.item_product_choice_option.view.*

class OptionsAdapter(private val context: Context) : RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {

    private val items by lazy { mutableListOf<AppProductChoiceOption>() }
    var onSelected: ((option: AppProductChoiceOption, checked: Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.item_product_choice_option))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], context)
    }

    fun setItems(newItems: List<AppProductChoiceOption>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(option: AppProductChoiceOption, context: Context) {
            itemView.run {
                val formattedPrice = CurrencyFormatter.format(option.price)
                tvOptionPrice.text = context.getString(R.string.price_listing_w_add, formattedPrice)
                cbOptionTitle.text = option.name
                cbOptionTitle.isChecked = option.isChecked

                cbOptionTitle.setOnClickListener {
                    onSelected?.invoke(option, cbOptionTitle.isChecked)
                }
            }
        }
    }

    private fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }
}
