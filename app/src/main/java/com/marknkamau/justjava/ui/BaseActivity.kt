package com.marknkamau.justjava.ui

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.AuthRepository
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.about.AboutActivity
import com.marknkamau.justjava.ui.cart.CartActivity
import com.marknkamau.justjava.ui.login.LogInActivity
import com.marknkamau.justjava.ui.profile.ProfileActivity
import com.marknkamau.justjava.utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.android.ext.android.inject

abstract class BaseActivity : AppCompatActivity() {

    private val authRepository: AuthRepository by inject()
    private val preferencesRepository: PreferencesRepository by inject()
    private val googleSignInClient: GoogleSignInClient by inject()
    private val coroutineScope by lazy { CoroutineScope(Dispatchers.Main) }

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
                    startActivity(Intent(this, ProfileActivity::class.java))
                } else {
                    startActivity(Intent(this, LogInActivity::class.java))
                }
                return true
            }
            R.id.menu_logout -> {
                logout()
                return true
            }
            R.id.menu_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        val signedInWithGoogle = preferencesRepository.user!!.signInMethod == "GOOGLE"

        coroutineScope.launch {
            when (val resource = authRepository.signOut()) {
                is Resource.Success -> {
                    if (signedInWithGoogle) {
                        googleSignInClient.signOut().await()
                    }

                    // If this is ProfileActivity, leave it
                    (this@BaseActivity as? ProfileActivity)?.finish()
                    toast("Logged out")

                    invalidateOptionsMenu()
                }
                is Resource.Failure -> toast(resource.message)
            }

        }
    }

}
