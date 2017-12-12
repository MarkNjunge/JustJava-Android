package com.marknkamau.justjava.ui

import android.content.Intent
import android.os.Bundle

import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.about.AboutActivity
import com.marknkamau.justjava.ui.cart.CartActivity
import com.marknkamau.justjava.ui.login.LogInActivity
import com.marknkamau.justjava.ui.profile.ProfileActivity
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    private var isSignedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as JustJavaApp).authService.addAuthListener(this)
    }

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
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_cart -> {
                startActivity(Intent(this, CartActivity::class.java))
                return true
            }
            R.id.menu_profile -> {
                if (isSignedIn) {
                    startActivity(Intent(this, ProfileActivity::class.java))
                } else {
                    startActivity(Intent(this, LogInActivity::class.java))
                }
                return true
            }
            R.id.menu_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        Timber.i("Auth state changed")
        isSignedIn = firebaseAuth.currentUser != null
    }
}
