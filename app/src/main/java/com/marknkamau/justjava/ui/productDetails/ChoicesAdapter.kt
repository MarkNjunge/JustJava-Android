package com.marknkamau.justjava.ui.productDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marknkamau.justjava.data.models.AppProductChoice
import com.marknkamau.justjava.databinding.ItemProductChoiceBinding
import com.marknkamau.justjava.utils.replace

class ChoicesAdapter(private val context: Context) : RecyclerView.Adapter<ChoicesAdapter.ViewHolder>() {
    private val items by lazy { mutableListOf<AppProductChoice>() }
    lateinit var onChoiceUpdated: (choice: AppProductChoice) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductChoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], context)
    }

    fun setItems(newItems: List<AppProductChoice>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemProductChoiceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(choice: AppProductChoice, context: Context) {
            // Set choice details
            binding.tvChoiceTitle.text = choice.name
            binding.tvChoiceRequired.visibility = if (choice.isRequired) View.VISIBLE else View.GONE

            // Set options adapter
            val optionsAdapter = OptionsAdapter(context)
            binding.rvChoiceOptions.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding.rvChoiceOptions.adapter = optionsAdapter

            // If a choice is required, auto select the first element
            if (choice.isRequired) {
                choice.options = choice.options.replace(choice.options[0], choice.options[0].copy(isChecked = true))
            }

            // Set items to the adapter
            optionsAdapter.setItems(choice.options)

            optionsAdapter.onSelected = { option, checked ->
                choice.options = choice.options
                    .map {
                        // If only 1 value can be selected, reset all to unchecked
                        // otherwise, keep as is
                        if (choice.isSingleSelectable) {
                            it.copy(isChecked = false)
                        } else {
                            it
                        }
                    }
                    .map {
                        // When the same option is arrived at, change it to the new state,
                        // otherwise, keep as is
                        if (it.id == option.id) {
                            it.copy(isChecked = checked)
                        } else {
                            it
                        }
                    }

                optionsAdapter.setItems(choice.options)

                onChoiceUpdated(choice)
            }
        }
    }
}
