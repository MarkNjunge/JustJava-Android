package com.marknkamau.justjava.ui.checkout

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.marknkamau.justjava.BuildConfig

import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.R
import com.marknkamau.justjava.models.Order
import com.marknkamau.justjava.models.UserDetails
import com.marknkamau.justjava.ui.BaseActivity
import com.marknkamau.justjava.ui.login.LogInActivity
import com.marknkamau.justjava.ui.main.MainActivity

import com.marknkamau.justjava.utils.trimmedText
import kotlinx.android.synthetic.main.activity_checkout.*
import java.util.*

class CheckoutActivity : BaseActivity(), CheckoutView {
    private lateinit var name: String
    private lateinit var phone: String
    private lateinit var address: String
    private lateinit var comments: String
    private lateinit var presenter: CheckoutPresenter

    private var payCash = true
    private val orderId = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val authService = (application as JustJavaApp).authService
        val preferencesRepo = (application as JustJavaApp).preferencesRepo
        val database = (application as JustJavaApp).databaseService
        val mpesaService = (application as JustJavaApp).mpesaService
        val cart = (application as JustJavaApp).cartDatabase.cartDao()

        presenter = CheckoutPresenter(this, authService, preferencesRepo, database, mpesaService, cart)

        rgPayment.setOnCheckedChangeListener { _, checkedId ->
            val text = findViewById<RadioButton>(checkedId).text
            payCash = text == getString(R.string.cash_on_delivery)
            btnPay.visibility = if (payCash) View.GONE else View.VISIBLE
        }

        btnPlaceOrder.setOnClickListener {
            if (canConnectToInternet()) {
                placeOder()
            } else {
                Toast.makeText(this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show()
            }
        }

        btnPay.setOnClickListener {
            val phoneNumber = etPhone.trimmedText
            if (phoneNumber.isNotEmpty()) {
                val dialog = AlertDialog.Builder(this@CheckoutActivity)
                        .setMessage("Are you sure you want to pay Ksh. 1 using $phoneNumber?")
                        .setTitle("Confirm payment")
                        .setPositiveButton("Ok", { _, _ ->
                            presenter.makeMpesaPayment(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET, "1", phoneNumber, orderId)
                        })
                        .setNegativeButton("cancel", { dialogInterface, _ -> dialogInterface.dismiss() })
                        .create()

                dialog.show()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        presenter.getSignInStatus()
    }

    override fun onStop() {
        super.onStop()
        presenter.unSubscribe()
    }

    private fun placeOder() {
        if (validateInput()) {
            presenter.placeOrder(Order(orderId, name, phone, 0, 0, address, comments))
        }
    }

    override fun setDisplayToLoggedIn(userDetails: UserDetails) {
        etName.setText(userDetails.name)
        etPhone.setText(userDetails.phone)
        etDeliveryAddress.setText(userDetails.address)
    }

    override fun invalidateMenu() {
        invalidateOptionsMenu()
    }

    override fun setDisplayToLoggedOut() {
        startActivity(Intent(this, LogInActivity::class.java))
        finish()
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

        var returnValue = true
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
