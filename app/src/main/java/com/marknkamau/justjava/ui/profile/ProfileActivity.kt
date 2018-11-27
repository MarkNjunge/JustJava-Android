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
import com.marknjunge.core.model.Order
import com.marknjunge.core.model.UserDetails
import com.marknkamau.justjava.ui.BaseActivity
import com.marknkamau.justjava.ui.previousOrder.PreviousOrderActivity

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

        adapter = PreviousOrderAdapter(this) { order ->
            PreviousOrderActivity.start(this, order)
        }

        rvPreviousOrders.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rvPreviousOrders.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        rvPreviousOrders.adapter = adapter

        val preferencesRepository = (application as JustJavaApp).preferencesRepo
        val auth = (application as JustJavaApp).authService
        val database = (application as JustJavaApp).databaseService
        presenter = ProfilePresenter(this, preferencesRepository, auth, database)

        btnUpdateProfile.setOnClickListener { saveChanges() }
    }

    override fun showOrdersProgressBar() {
        pbLoadingOrders.visibility = View.VISIBLE
    }

    override fun hideOrdersProgressBar() {
        pbLoadingOrders.visibility = View.GONE
    }

    override fun showProfileProgressBar() {
        pbUpdatingProject.visibility = View.VISIBLE
    }

    override fun hideProfileProgressBar() {
        pbUpdatingProject.visibility = View.GONE
    }

    override fun displayNoPreviousOrders() {
        tvNoOrders.visibility = View.VISIBLE
        rvPreviousOrders.visibility = View.GONE
    }

    override fun displayPreviousOrders(orderList: MutableList<Order>) {
        adapter.setItems(orderList)
    }

    override fun displayMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveChanges() {
        if (fieldsOk()) {
            presenter.updateUserDetails(name!!, phone!!, address!!)
        }
    }

    private fun fieldsOk(): Boolean {
        name = etName.trimmedText
        phone = etPhone.trimmedText
        address = etDeliveryAddress.trimmedText

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun displayUserDetails(userDetails: UserDetails) {
        etName.setText(userDetails.name)
        etPhone.setText(userDetails.phone)
        etDeliveryAddress.setText(userDetails.address)
    }
}