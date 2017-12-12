package com.marknkamau.justjava.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marknkamau.justjava.R
import com.marknkamau.justjava.models.CoffeeDrink
import com.marknkamau.justjava.ui.drinkdetails.DrinkDetailsActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_catalog.view.*

class CatalogAdapter(private val mContext: Context, private val drinkList: List<CoffeeDrink>) : RecyclerView.Adapter<CatalogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val itemView = inflater.inflate(R.layout.item_catalog, parent, false)

        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(drinkList[position])

    override fun getItemCount(): Int {
        return drinkList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(drink: CoffeeDrink) {
            itemView.tvItemName.text = drink.drinkName
            itemView.tvShortDesc.text = drink.drinkContents
            itemView.tvDrinkPrice.text = mContext.resources.getString(R.string.ksh) + drink.drinkPrice

            val drinkImage = "file:///android_asset/" + drink.drinkImage
            Picasso.with(mContext).load(drinkImage).placeholder(R.drawable.plain_brown).into(itemView.imgDrinkImage)

            itemView.catalogItem.setOnClickListener {
                val i = Intent(mContext, DrinkDetailsActivity::class.java)
                i.putExtra(DRINK_KEY, drink)
                mContext.startActivity(i)
            }
        }

    }

    companion object {
        val DRINK_KEY = "drink_key"
    }
}
