package com.marknkamau.justjava.ui.checkout

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.google.firebase.auth.FirebaseUser
import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.PreferencesRepository
import com.marknkamau.justjava.models.Order
import com.marknkamau.justjava.models.UserDefaults
import com.marknkamau.justjava.ui.about.AboutActivity
import com.marknkamau.justjava.ui.cart.CartActivity
import com.marknkamau.justjava.ui.login.LogInActivity
import com.marknkamau.justjava.ui.main.MainActivity
import com.marknkamau.justjava.ui.profile.ProfileActivity

import com.marknkamau.justjava.utils.bindView

class CheckoutActivity : AppCompatActivity(), CheckoutView, View.OnClickListener {
    val toolbar: Toolbar by bindView(R.id.toolbar)
    val btnLogIn: Button by bindView(R.id.btn_log_in)
    val tvOr: TextView by bindView(R.id.tv_or)
    val etName: EditText by bindView(R.id.et_name)
    val etPhoneNumber: EditText by bindView(R.id.et_phone_number)
    val etDeliveryAddress: EditText by bindView(R.id.et_delivery_address)
    val etComments: MultiAutoCompleteTextView by bindView(R.id.et_comments)
    val pbProgress: ProgressBar by bindView(R.id.pb_progress)
    val btnPlaceOrder: Button by bindView(R.id.btn_place_order)

    private var name: String? = null
    private var phone: String? = null
    private var address: String? = null
    private var comments: String? = null
    private lateinit var presenter: CheckoutPresenter
    private var userIsLoggedIn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val preferencesRepository: PreferencesRepository = (application as JustJavaApp).preferencesRepo
        presenter = CheckoutPresenter(this, preferencesRepository)

        btnLogIn.setOnClickListener(this)
        btnPlaceOrder.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        if (userIsLoggedIn) {
            inflater.inflate(R.menu.toolbar_menu_logged_in, menu)
        } else {
            inflater.inflate(R.menu.toolbar_menu, menu)
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        presenter.updateLoggedInStatus()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_cart -> {
                startActivity(Intent(this@CheckoutActivity, CartActivity::class.java))
                finish()
                return true
            }
            R.id.menu_log_in -> {
                startActivity(Intent(this, LogInActivity::class.java))
                return true
            }
            R.id.menu_log_out -> {
                presenter.logOut()
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
            presenter.placeOrder(Order(name!!, phone!!, 0, 0, address!!, comments!!))
        }
    }

    override fun setLoggedInStatus(status: Boolean) {
        userIsLoggedIn = status
    }

    override fun setDisplayToLoggedIn(user: FirebaseUser, userDefaults: UserDefaults) {
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

    override fun showMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun validateInput(): Boolean {
        name = etName.text.toString().trim()
        phone = etPhoneNumber.text.toString().trim()
        address = etDeliveryAddress.text.toString().trim()
        comments = etComments.text.toString().trim()

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
