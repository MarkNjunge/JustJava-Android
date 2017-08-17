package com.marknkamau.justjava.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem

import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.about.AboutActivity
import com.marknkamau.justjava.ui.cart.CartActivity
import com.marknkamau.justjava.ui.login.LogInActivity
import com.marknkamau.justjava.ui.profile.ProfileActivity
import com.marknkamau.justjava.models.CoffeeDrink

import com.marknkamau.justjava.utils.bindView

class MainActivity : AppCompatActivity(), MainView {
    val toolbar: Toolbar by bindView(R.id.toolbar)
    val rvCatalog: RecyclerView by bindView(R.id.rv_catalog)

    private lateinit var presenter: MainPresenter
    private var isSignedIn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        rvCatalog.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val preferencesRepository = (application as JustJavaApp).preferencesRepo
        val authenticationService = (application as JustJavaApp).authService

        presenter = MainPresenter(this, preferencesRepository, authenticationService)
        presenter.getCatalogItems()
    }

    override fun onResume() {
        super.onResume()
        presenter.getSignInStatus()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        if (isSignedIn) {
            inflater.inflate(R.menu.toolbar_menu_logged_in, menu)
        } else {
            inflater.inflate(R.menu.toolbar_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_cart -> {
                startActivity(Intent(this@MainActivity, CartActivity::class.java))
                return true
            }
            R.id.menu_log_in -> {
                startActivity(Intent(this, LogInActivity::class.java))
                return true
            }
            R.id.menu_log_out -> {
                presenter.signOut()
                return true
            }
            R.id.menu_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                return true
            }
            R.id.menu_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun displayCatalog(drinkList: MutableList<CoffeeDrink>) {
        rvCatalog.adapter = CatalogAdapter(this, drinkList)
    }

    override fun setSignInStatus(status: Boolean) {
        isSignedIn = status
        invalidateOptionsMenu()
    }
}
