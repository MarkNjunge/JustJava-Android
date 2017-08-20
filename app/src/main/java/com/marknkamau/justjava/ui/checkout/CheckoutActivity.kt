package com.marknkamau.justjava.ui.checkout

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.R
import com.marknkamau.justjava.models.Order
import com.marknkamau.justjava.models.UserDefaults
import com.marknkamau.justjava.ui.BaseActivity
import com.marknkamau.justjava.ui.login.LogInActivity
import com.marknkamau.justjava.ui.main.MainActivity

import com.marknkamau.justjava.utils.bindView
import com.marknkamau.justjava.utils.trimmedText

class CheckoutActivity : BaseActivity(), CheckoutView, View.OnClickListener {
    val toolbar: Toolbar by bindView(R.id.toolbar)
    val btnLogIn: Button by bindView(R.id.btn_log_in)
    val tvOr: TextView by bindView(R.id.tv_or)
    val etName: EditText by bindView(R.id.et_name)
    val etPhoneNumber: EditText by bindView(R.id.et_phone_number)
    val etDeliveryAddress: EditText by bindView(R.id.et_delivery_address)
    val etComments: MultiAutoCompleteTextView by bindView(R.id.et_comments)
    val pbProgress: ProgressBar by bindView(R.id.pb_progress)
    val btnPlaceOrder: Button by bindView(R.id.btn_place_order)

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

        btnLogIn.setOnClickListener(this)
        btnPlaceOrder.setOnClickListener(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.unSubscribe()
    }

    override fun onClick(view: View) {
        when (view) {
            btnLogIn -> startActivity(Intent(this@CheckoutActivity, LogInActivity::class.java))
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
        btnLogIn.visibility = View.GONE

        etName.setText(userDefaults.name)
        etPhoneNumber.setText(userDefaults.phone)
        etDeliveryAddress.setText(userDefaults.defaultAddress)
    }

    override fun invalidateMenu() {
        invalidateOptionsMenu()
    }

    override fun setDisplayToLoggedOut() {
        tvOr.text = getString(R.string.or)
        btnLogIn.visibility = View.VISIBLE
    }

    override fun showUploadBar() {
        pbProgress.visibility = View.VISIBLE
        btnPlaceOrder.setBackgroundResource(R.drawable.large_button_disabled)
        btnPlaceOrder.isEnabled = false
    }

    override fun hideUploadBar() {
        pbProgress.visibility = View.INVISIBLE
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
        name = etName.trimmedText()
        phone = etPhoneNumber.trimmedText()
        address = etDeliveryAddress.trimmedText()
        comments = etComments.trimmedText()

        var returnValue: Boolean = true
        if (TextUtils.isEmpty(name)) {
            etName.error = getString(R.string.required)
            returnValue = false
        }
        if (TextUtils.isEmpty(phone)) {
            etPhoneNumber.error = getString(R.string.required)
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
