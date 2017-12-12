package com.marknkamau.justjava.ui.main

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.marknkamau.justjava.R
import com.marknkamau.justjava.models.CoffeeDrink
import com.marknkamau.justjava.ui.BaseActivity

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainView {
    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        rvCatalog.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        presenter = MainPresenter(this)
        presenter.getCatalogItems()
    }

    override fun displayCatalog(drinkList: MutableList<CoffeeDrink>) {
        rvCatalog.adapter = CatalogAdapter(this, drinkList)
    }
}
