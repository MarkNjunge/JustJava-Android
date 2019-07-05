package com.marknkamau.justjava.ui.main

import android.os.Bundle
import androidx.core.util.Pair
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marknjunge.core.model.CoffeeDrink
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.BaseActivity
import com.marknkamau.justjava.ui.drinkdetails.DrinkDetailsActivity
import com.marknkamau.justjava.utils.BaseRecyclerViewAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_toolbar.*
import kotlinx.android.synthetic.main.item_catalog.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : BaseActivity(), MainView {
    private val presenter: MainPresenter by inject { parametersOf(this) }
    private lateinit var catalogAdapter: BaseRecyclerViewAdapter<CoffeeDrink>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        catalogAdapter = BaseRecyclerViewAdapter(R.layout.item_catalog) { drink ->
            tvDrinkNameCatalog.text = drink.drinkName
            tvDrinkContentsCatalog.text = drink.drinkContents
            tvDrinkPriceCatalog.text = context.resources.getString(R.string.price_listing, drink.drinkPrice.toInt())

            val drinkImage = "file:///android_asset/" + drink.drinkImage
            Picasso.get().load(drinkImage).placeholder(R.drawable.plain_brown).into(imgDrinkImageCatalog)

            catalogItem.setOnClickListener {
                DrinkDetailsActivity.start(
                        this@MainActivity,
                        drink,
                        Pair(imgDrinkImageCatalog, "drinkImage")
                )
            }
        }

        rvCatalog.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.custom_item_divider)!!)
        rvCatalog.addItemDecoration(dividerItemDecoration)
        rvCatalog.adapter = catalogAdapter

        presenter.getCatalogItems()
    }

    override fun displayCatalog(drinkList: MutableList<CoffeeDrink>) {
        catalogAdapter.setItems(drinkList)
    }
}
