package com.marknkamau.justjava.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marknjunge.core.model.Order
import com.marknjunge.core.model.UserDetails
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.BaseActivity
import com.marknkamau.justjava.ui.previousOrder.PreviousOrderActivity
import com.marknkamau.justjava.utils.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.content_toolbar.*
import kotlinx.android.synthetic.main.item_previous_order.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ProfileActivity : BaseActivity(), ProfileView {
    private lateinit var previousOrdersAdapter: BaseRecyclerViewAdapter<Order>

    private val presenter: ProfilePresenter by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)

        previousOrdersAdapter = BaseRecyclerViewAdapter(R.layout.item_previous_order) { order ->
            tvOrderTimeItem.text = order.date.formatForApp()
            tvOrderStatusItem.text = order.status.name.toLowerCase().capitalize()
            tvOrderQtyItem.text = order.itemsCount.toString()
            tvOrderCountItem.text = resources.getQuantityString(R.plurals.order_info, order.itemsCount)
            tvOrderTotalItem.text = resources.getString(R.string.price_listing, order.totalPrice)

            previousOrderItemRootLayout.setOnClickListener {
                PreviousOrderActivity.start(this@ProfileActivity, order)
            }
        }

        rvPreviousOrdersProfile.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvPreviousOrdersProfile.addItemDecoration(DividerItemDecorator(getDrawable(R.drawable.custom_item_divider)!!))
        rvPreviousOrdersProfile.adapter = previousOrdersAdapter

        presenter.getUserDetails()
        presenter.getPreviousOrders()

        btnUpdateProfile.setOnClickListener { saveChanges() }

        etNameProfile.onTextChanged { tilNameProfile.error = null }
        etPhoneProfile.onTextChanged { tilPhoneProfile.error = null }
        etAddressProfile.onTextChanged { tilAddressProfile.error = null }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.cancel()
    }

    override fun showProfileProgressBar() {
        pbUpdatingProfile.visibility = View.VISIBLE
    }

    override fun hideProfileProgressBar() {
        pbUpdatingProfile.visibility = View.GONE
    }

    override fun displayNoPreviousOrders() {
        pbLoadingOrdersProfile.visibility = View.GONE
        contentNoOrdersProfile.visibility = View.VISIBLE
        rvPreviousOrdersProfile.visibility = View.GONE
    }

    override fun displayPreviousOrders(orderList: MutableList<Order>) {
        pbLoadingOrdersProfile.visibility = View.GONE
        contentNoOrdersProfile.visibility = View.GONE
        rvPreviousOrdersProfile.visibility = View.VISIBLE
        previousOrdersAdapter.setItems(orderList)
    }

    override fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveChanges() {
        if (fieldsOk()) {
            hideKeyboard()
            presenter.updateUserDetails(etNameProfile.trimmedText, etPhoneProfile.trimmedText, etAddressProfile.trimmedText)
        }
    }

    private fun fieldsOk(): Boolean {
        var isValid = true

        if (etNameProfile.trimmedText.isEmpty()) {
            isValid = false
            tilNameProfile.error = getString(R.string.required)
        }

        if (etPhoneProfile.trimmedText.isEmpty()) {
            isValid = false
            tilPhoneProfile.error = getString(R.string.required)
        }

        if (etAddressProfile.trimmedText.isEmpty()) {
            isValid = false
            tilAddressProfile.error = getString(R.string.required)
        }

        return isValid
    }

    override fun displayUserDetails(userDetails: UserDetails) {
        etNameProfile.setText(userDetails.name)
        etPhoneProfile.setText(userDetails.phone)
        etAddressProfile.setText(userDetails.address)
    }
}