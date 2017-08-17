package com.marknkamau.justjava.ui.profile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.google.firebase.auth.FirebaseUser
import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.PreferencesRepository
import com.marknkamau.justjava.models.PreviousOrder
import com.marknkamau.justjava.models.UserDefaults
import com.marknkamau.justjava.ui.about.AboutActivity
import com.marknkamau.justjava.ui.cart.CartActivity
import com.marknkamau.justjava.ui.login.LogInActivity
import com.marknkamau.justjava.network.AuthenticationServiceImpl

import javax.inject.Inject

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.marknkamau.justjava.utils.bindView

class ProfileActivity : AppCompatActivity(), ProfileView {
    val toolbar: Toolbar by bindView(R.id.toolbar)
    val etName: EditText by bindView(R.id.et_name)
    val etPhoneNumber: EditText by bindView(R.id.et_phone_number)
    val etDeliveryAddress: EditText by bindView(R.id.et_delivery_address)
    val pbSaving: ProgressBar by bindView(R.id.pb_saving)
    val btnSave: Button by bindView(R.id.btn_save)
    val rvPreviousOrders: RecyclerView by bindView(R.id.rv_previous_orders)
    val tvNoOrders: TextView by bindView(R.id.tv_no_orders)
    val pbLoadingOrders: ProgressBar by bindView(R.id.pb_loading_orders)

    private var user: FirebaseUser? = null
    private var name: String? = null
    private var phone: String? = null
    private var address: String? = null
    private lateinit var presenter: ProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        user = AuthenticationServiceImpl.currentUser

        if (user == null) {
            finish()
        }


        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        rvPreviousOrders.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        rvPreviousOrders.addItemDecoration(itemDecoration)

        val preferencesRepository = (application as JustJavaApp).preferencesRepo
        presenter = ProfilePresenter(this, preferencesRepository)

        btnSave.setOnClickListener { saveChanges() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        user = AuthenticationServiceImpl.currentUser
        val inflater = menuInflater
        if (user == null) {
            inflater.inflate(R.menu.toolbar_menu, menu)
        } else {
            inflater.inflate(R.menu.toolbar_menu_logged_in, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_cart -> {
                startActivity(Intent(this@ProfileActivity, CartActivity::class.java))
                return true
            }
            R.id.menu_log_in -> {
                startActivity(Intent(this, LogInActivity::class.java))
                return true
            }
            R.id.menu_log_out -> {
                presenter.logUserOut()
                finish()
                return true
            }
            R.id.menu_profile ->
                // Do nothing
                return true
            R.id.menu_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun showProgressBar() {
        pbSaving.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        pbSaving.visibility = View.INVISIBLE
    }

    override fun displayNoPreviousOrders() {
        pbLoadingOrders.visibility = View.GONE
        tvNoOrders.visibility = View.VISIBLE
        rvPreviousOrders.visibility = View.GONE
        Toast.makeText(this, "No previous orders", Toast.LENGTH_SHORT).show()
    }

    override fun displayPreviousOrders(orderList: List<PreviousOrder>) {
        pbLoadingOrders.visibility = View.GONE
        rvPreviousOrders.adapter = PreviousOrderAdapter(this, orderList)
    }

    override fun displayMessage(message: String?) {
        pbSaving.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveChanges() {
        if (fieldsOk()) {
            pbSaving.visibility = View.VISIBLE
            presenter.updateUserDefaults(name!!, phone!!, address!!)
        }
    }

    private fun fieldsOk(): Boolean {
        name = etName.text.toString().trim { it <= ' ' }
        phone = etPhoneNumber.text.toString().trim { it <= ' ' }
        address = etDeliveryAddress.text.toString().trim { it <= ' ' }

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun displayUserDefaults(userDefaults: UserDefaults) {
        etName.setText(userDefaults.name)
        etPhoneNumber.setText(userDefaults.phone)
        etDeliveryAddress.setText(userDefaults.defaultAddress)
    }
}