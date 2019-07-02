package com.marknkamau.justjava.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marknkamau.justjava.R
import com.marknjunge.core.model.CoffeeDrink
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_catalog.view.*

class OldCatalogAdapter(private val context: Context, private val onClick: (CoffeeDrink) -> Unit) : androidx.recyclerview.widget.RecyclerView.Adapter<OldCatalogAdapter.ViewHolder>() {

    private val items by lazy { mutableListOf<CoffeeDrink>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_catalog))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], context, onClick)

    override fun getItemCount() = items.size

    fun setItems(items: MutableList<CoffeeDrink>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(drink: CoffeeDrink, context: Context, onClick: (CoffeeDrink) -> Unit) {
            itemView.tvItemName.text = drink.drinkName
            itemView.tvShortDesc.text = drink.drinkContents
            itemView.tvDrinkName.text = context.resources.getString(R.string.price_listing, drink.drinkPrice.toInt())

            val drinkImage = "file:///android_asset/" + drink.drinkImage
            Picasso.get().load(drinkImage).placeholder(R.drawable.plain_brown).into(itemView.imgDrinkImage)

            itemView.catalogItem.setOnClickListener {
                onClick(drink)
            }
        }
    }

    private fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }
}
