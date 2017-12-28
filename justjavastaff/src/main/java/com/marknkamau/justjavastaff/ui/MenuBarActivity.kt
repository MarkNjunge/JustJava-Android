package com.marknkamau.justjavastaff.ui

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import com.marknkamau.justjavastaff.R

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

@SuppressLint("Registered")
open class MenuBarActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_log_out ->

                true
            R.id.menu_settings ->

                true
            R.id.menu_help ->

                true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
