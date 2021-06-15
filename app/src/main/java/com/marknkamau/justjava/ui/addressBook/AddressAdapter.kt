package com.marknkamau.justjava.ui.addressBook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marknjunge.core.data.model.Address
import com.marknkamau.justjava.databinding.ItemAddressBinding

class AddressAdapter(
    private val deleteAddress: (address: Address) -> Unit
) : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    val items = mutableListOf<Address>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, deleteAddress)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setItems(newItems: List<Address>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemAddressBinding,
        private val deleteAddress: (Address) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(address: Address) {
            binding.tvStreetAddress.text = address.streetAddress
            binding.tvDeliveryInstructions.text = address.deliveryInstructions
            binding.tvDeliveryInstructions.visibility =
                if (address.deliveryInstructions == null) View.GONE else View.VISIBLE

            binding.root.setOnLongClickListener {
                deleteAddress(address)
                true
            }
        }
    }
}