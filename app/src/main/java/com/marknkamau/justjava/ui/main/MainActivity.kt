package com.marknkamau.justjava.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.marknkamau.justjava.R
import com.marknjunge.core.model.CoffeeDrink
import com.marknkamau.justjava.ui.BaseActivity
import com.marknkamau.justjava.ui.drinkdetails.DrinkDetailsActivity

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainView {
    private lateinit var presenter: MainPresenter
    private lateinit var catalogAdapter: CatalogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        catalogAdapter = CatalogAdapter(this) { coffeeDrink ->
            val i = Intent(this, DrinkDetailsActivity::class.java)
            i.putExtra(DrinkDetailsActivity.DRINK_KEY, coffeeDrink)
            startActivity(i)
        }

        rvCatalog.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        rvCatalog.adapter = catalogAdapter

        presenter = MainPresenter(this)
        presenter.getCatalogItems()
    }

    override fun displayCatalog(drinkList: MutableList<CoffeeDrink>) {
        catalogAdapter.setItems(drinkList)
    }
}
