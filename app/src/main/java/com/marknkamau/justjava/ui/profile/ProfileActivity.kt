package com.marknkamau.justjava.ui.profile

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast

import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.R
import com.marknkamau.justjava.models.PreviousOrder
import com.marknkamau.justjava.models.UserDefaults
import com.marknkamau.justjava.ui.BaseActivity

import com.marknkamau.justjava.utils.trimmedText
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity(), ProfileView {
    private var name: String? = null
    private var phone: String? = null
    private var address: String? = null
    private lateinit var presenter: ProfilePresenter
    private lateinit var adapter: PreviousOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        adapter = PreviousOrderAdapter(this)

        rvPreviousOrders.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rvPreviousOrders.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        rvPreviousOrders.adapter = adapter

        val preferencesRepository = (application as JustJavaApp).preferencesRepo
        presenter = ProfilePresenter(this, preferencesRepository)

        btnSave.setOnClickListener { saveChanges() }
        btnLogout.setOnClickListener {
            preferencesRepository.clearDefaults()
            presenter.logUserOut()
            finish()
        }
    }

    override fun showProgressBar() {
        pbLoadingOrders.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        pbLoadingOrders.visibility = View.INVISIBLE
    }

    override fun displayNoPreviousOrders() {
        pbLoadingOrders.visibility = View.GONE
        tvNoOrders.visibility = View.VISIBLE
        rvPreviousOrders.visibility = View.GONE
//        Toast.makeText(this, "No previous orders", Toast.LENGTH_SHORT).show()
    }

    override fun displayPreviousOrders(orderList: MutableList<PreviousOrder>) {
        pbLoadingOrders.visibility = View.GONE
        adapter.setItems(orderList)
    }

    override fun displayMessage(message: String?) {
        pbLoadingOrders.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveChanges() {
        if (fieldsOk()) {
            pbLoadingOrders.visibility = View.VISIBLE
            presenter.updateUserDefaults(name!!, phone!!, address!!)
        }
    }

    private fun fieldsOk(): Boolean {
        name = etName.trimmedText()
        phone = etPhone.trimmedText()
        address = etDeliveryAddress.trimmedText()

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun displayUserDefaults(userDefaults: UserDefaults) {
        etName.setText(userDefaults.name)
        etPhone.setText(userDefaults.phone)
        etDeliveryAddress.setText(userDefaults.defaultAddress)
    }
}