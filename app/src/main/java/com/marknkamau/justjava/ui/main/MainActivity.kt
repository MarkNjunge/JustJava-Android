package com.marknkamau.justjava.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.models.CoffeeDrink
import com.marknkamau.justjava.ui.BaseActivity
import com.marknkamau.justjava.ui.drinkdetails.DrinkDetailsActivity

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_appbar.*

class MainActivity : BaseActivity(), MainView {
    private lateinit var presenter: MainPresenter
    private lateinit var catalogAdapter: CatalogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        catalogAdapter = CatalogAdapter(this) { coffeeDrink ->
            val i = Intent(this, DrinkDetailsActivity::class.java)
            i.putExtra(DrinkDetailsActivity.DRINK_KEY, coffeeDrink)
            startActivity(i)
        }

        rvCatalog.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvCatalog.adapter = catalogAdapter

        presenter = MainPresenter(this)
        presenter.getCatalogItems()
    }

    override fun displayCatalog(drinkList: MutableList<CoffeeDrink>) {
        catalogAdapter.setItems(drinkList)
    }
}
