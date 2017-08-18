package com.marknkamau.justjava.ui.main

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.marknkamau.justjava.R
import com.marknkamau.justjava.models.CoffeeDrink
import com.marknkamau.justjava.ui.BaseActivity

import com.marknkamau.justjava.utils.bindView

class MainActivity : BaseActivity(), MainView {
    val toolbar: Toolbar by bindView(R.id.toolbar)
    val rvCatalog: RecyclerView by bindView(R.id.rv_catalog)

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
