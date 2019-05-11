package com.marknkamau.justjava.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marknkamau.justjava.R
import com.marknjunge.core.model.CoffeeDrink
import com.marknkamau.justjava.ui.BaseActivity
import com.marknkamau.justjava.ui.drinkdetails.DrinkDetailsActivity

import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : BaseActivity(), MainView {
    private val presenter: MainPresenter by inject { parametersOf(this) }
    private lateinit var catalogAdapter: CatalogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        catalogAdapter = CatalogAdapter(this) { coffeeDrink ->
            val i = Intent(this, DrinkDetailsActivity::class.java)
            i.putExtra(DrinkDetailsActivity.DRINK_KEY, coffeeDrink)
            startActivity(i)
        }

        rvCatalog.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvCatalog.adapter = catalogAdapter

        presenter.getCatalogItems()
    }

    override fun displayCatalog(drinkList: MutableList<CoffeeDrink>) {
        catalogAdapter.setItems(drinkList)
    }
}
