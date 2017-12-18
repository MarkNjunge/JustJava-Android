package com.marknkamau.justjava.ui.checkout

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast

import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.R
import com.marknkamau.justjava.models.Order
import com.marknkamau.justjava.models.UserDefaults
import com.marknkamau.justjava.ui.BaseActivity
import com.marknkamau.justjava.ui.login.LogInActivity
import com.marknkamau.justjava.ui.main.MainActivity

import com.marknkamau.justjava.utils.trimmedText
import kotlinx.android.synthetic.main.activity_checkout.*

class CheckoutActivity : BaseActivity(), CheckoutView, View.OnClickListener {
    private lateinit var name: String
    private lateinit var phone: String
    private lateinit var address: String
    private lateinit var comments: String
    private lateinit var presenter: CheckoutPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val authService = (application as JustJavaApp).authService
        val preferencesRepo = (application as JustJavaApp).preferencesRepo
        val database = (application as JustJavaApp).databaseService
        val cart = (application as JustJavaApp).cartDatabase.cartDao()

        presenter = CheckoutPresenter(this, authService, preferencesRepo, database, cart)
        presenter.getSignInStatus()

        btnLogin.setOnClickListener(this)
        btnPlaceOrder.setOnClickListener(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.unSubscribe()
    }

    override fun onClick(view: View) {
        when (view) {
            btnLogin -> startActivity(Intent(this@CheckoutActivity, LogInActivity::class.java))
            btnPlaceOrder -> if (canConnectToInternet()) {
                placeOder()
            } else {
                Toast.makeText(this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun placeOder() {
        if (validateInput()) {
            presenter.placeOrder(Order(name, phone, 0, 0, address, comments))
        }
    }

    override fun setDisplayToLoggedIn(userDefaults: UserDefaults) {
        tvOr.visibility = View.GONE
        btnLogin.visibility = View.GONE

        etName.setText(userDefaults.name)
        etPhone.setText(userDefaults.phone)
        etDeliveryAddress.setText(userDefaults.defaultAddress)
    }

    override fun invalidateMenu() {
        invalidateOptionsMenu()
    }

    override fun setDisplayToLoggedOut() {
        tvOr.text = getString(R.string.or)
        btnLogin.visibility = View.VISIBLE
    }

    override fun showUploadBar() {
        pbSavingProfile.visibility = View.VISIBLE
        btnPlaceOrder.setBackgroundResource(R.drawable.large_button_disabled)
        btnPlaceOrder.isEnabled = false
    }

    override fun hideUploadBar() {
        pbSavingProfile.visibility = View.INVISIBLE
        btnPlaceOrder.setBackgroundResource(R.drawable.large_button)
        btnPlaceOrder.isEnabled = true
    }

    override fun finishActivity() {
        val intent = Intent(this@CheckoutActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
    }

    override fun displayMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun validateInput(): Boolean {
        name = etName.trimmedText
        phone = etPhone.trimmedText
        address = etDeliveryAddress.trimmedText
        comments = etComments.trimmedText

        var returnValue: Boolean = true
        if (TextUtils.isEmpty(name)) {
            etName.error = getString(R.string.required)
            returnValue = false
        }
        if (TextUtils.isEmpty(phone)) {
            etPhone.error = getString(R.string.required)
            returnValue = false
        }
        if (TextUtils.isEmpty(address)) {
            etDeliveryAddress.error = getString(R.string.required)
            returnValue = false
        }
        return returnValue
    }

    private fun canConnectToInternet(): Boolean {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}
