package com.marknkamau.justjava.ui

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.repository.UsersRepository
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.about.AboutActivity
import com.marknkamau.justjava.ui.cart.CartActivity
import com.marknkamau.justjava.ui.login.LogInActivity
import com.marknkamau.justjava.ui.profile.ProfileActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

abstract class BaseActivity : AppCompatActivity() {

    private val usersRepository:UsersRepository by inject()
    private val preferencesRepository: PreferencesRepository by inject()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        if (this is CartActivity) {
            menu?.findItem(R.id.menu_cart)?.isVisible = false
        }
        if (this is ProfileActivity) {
            menu?.findItem(R.id.menu_profile)?.isVisible = false
        }

        menu?.findItem(R.id.menu_logout)?.isVisible = preferencesRepository.isSignedIn
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_cart -> {
                startActivity(Intent(this, CartActivity::class.java))
                return true
            }
            R.id.menu_profile -> {
                if (preferencesRepository.isSignedIn) {
                    // TODO go to profile
                    Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show()
                } else {
                    startActivity(Intent(this, LogInActivity::class.java))
                }
                return true
            }
            R.id.menu_logout -> {
                CoroutineScope(Dispatchers.IO).launch {
                    usersRepository.logout()
                }
                // If this is ProfileActivity, leave it
                (this as? ProfileActivity)?.finish()
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()

                invalidateOptionsMenu()
                return true
            }
            R.id.menu_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
