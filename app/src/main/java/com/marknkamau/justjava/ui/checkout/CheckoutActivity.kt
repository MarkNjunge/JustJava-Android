package com.marknkamau.justjava.ui.checkout

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.core.app.TaskStackBuilder
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.data.firebase.ClientDatabaseService
import com.marknkamau.justjava.R
import com.marknjunge.core.model.Order
import com.marknjunge.core.model.UserDetails
import com.marknkamau.justjava.data.local.CartDao
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknkamau.justjava.ui.BaseActivity
import com.marknkamau.justjava.ui.login.LogInActivity
import com.marknkamau.justjava.ui.main.MainActivity
import com.marknkamau.justjava.ui.previousOrder.PreviousOrderActivity
import com.marknkamau.justjava.utils.trimmedText
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.*

class CheckoutActivity : BaseActivity(), CheckoutView {
    private lateinit var name: String
    private lateinit var phone: String
    private lateinit var address: String
    private lateinit var comments: String

    private val presenter: CheckoutPresenter by inject { parametersOf(this) }

    private var payCash = true
    private val orderId = UUID.randomUUID().toString().replace("-", "").subSequence(0, 10).toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        supportActionBar?.title = "Checkout"

        rgPayment.setOnCheckedChangeListener { _, checkedId ->
            val text = findViewById<RadioButton>(checkedId).text
            payCash = text == getString(R.string.cash_on_delivery)
        }

        btnPlaceOrder.setOnClickListener {
            if (canConnectToInternet()) {
                placeOder()
            } else {
                Toast.makeText(this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        presenter.getSignInStatus()
    }

    override fun onStop() {
        super.onStop()
        presenter.cancel()
    }

    private fun placeOder() {
        if (validateInput()) {
            presenter.placeOrder(orderId, address, comments, payCash)
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
        btnPlaceOrder.isEnabled = false
    }

    override fun hideUploadBar() {
        pbSavingProfile.visibility = View.INVISIBLE
        btnPlaceOrder.isEnabled = true
    }

    override fun finishActivity(order: Order) {
        val i = Intent(this, PreviousOrderActivity::class.java)
        i.putExtra(PreviousOrderActivity.ORDER_KEY, order)

        TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(Intent(this, MainActivity::class.java))
                .addNextIntentWithParentStack(i)
                .startActivities()

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
