package com.marknkamau.justjava.ui.profile

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.data.firebase.ClientDatabaseService

import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.R
import com.marknjunge.core.model.Order
import com.marknjunge.core.model.UserDetails
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknkamau.justjava.ui.BaseActivity
import com.marknkamau.justjava.ui.previousOrder.PreviousOrderActivity

import com.marknkamau.justjava.utils.trimmedText
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ProfileActivity : BaseActivity(), ProfileView {
    private var name: String? = null
    private var phone: String? = null
    private var address: String? = null
    private lateinit var adapter: PreviousOrderAdapter

    private val presenter: ProfilePresenter by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        adapter = PreviousOrderAdapter(this) { order ->
            PreviousOrderActivity.start(this, order)
        }

        rvPreviousOrders.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvPreviousOrders.addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(this, LinearLayout.VERTICAL))
        rvPreviousOrders.adapter = adapter

        presenter.getUserDetails()
        presenter.getPreviousOrders()

        btnUpdateProfile.setOnClickListener { saveChanges() }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.cancel()
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