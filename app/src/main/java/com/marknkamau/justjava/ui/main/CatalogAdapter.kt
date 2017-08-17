package com.marknkamau.justjava.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.marknkamau.justjava.ui.drinkdetails.DrinkDetailsActivity
import com.marknkamau.justjava.R
import com.marknkamau.justjava.models.CoffeeDrink
import com.squareup.picasso.Picasso

import com.marknkamau.justjava.utils.bindView

class CatalogAdapter(private val mContext: Context, private val drinkList: List<CoffeeDrink>) : RecyclerView.Adapter<CatalogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val itemView = inflater.inflate(R.layout.item_catalog, parent, false)

        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val drink = drinkList[position]

        holder.tvItemName.text = drink.drinkName
        holder.tvShortDesc.text = drink.drinkContents
        holder.tvDrinkPrice.text = mContext.resources.getString(R.string.ksh) + drink.drinkPrice

        val drinkImage = "file:///android_asset/" + drink.drinkImage
        Picasso.with(mContext).load(drinkImage).placeholder(R.drawable.plain_brown).into(holder.imgDrinkImage)

        holder.catalogItem.setOnClickListener {
            val i = Intent(mContext, DrinkDetailsActivity::class.java)
            i.putExtra(DRINK_KEY, drink)
            mContext.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return drinkList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imgDrinkImage: ImageView by bindView(R.id.img_drink_image)
        val tvItemName: TextView by bindView(R.id.tv_item_name)
        val tvShortDesc: TextView by bindView(R.id.tv_short_desc)
        val tvDrinkPrice: TextView by bindView(R.id.tv_drink_price)
        val catalogItem: ConstraintLayout by bindView(R.id.catalog_item)
    }

    companion object {
        val DRINK_KEY = "drink_key"
    }
}
