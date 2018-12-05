package com.marknkamau.justjavastaff.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast

import com.marknkamau.justjavastaff.JustJavaStaffApp
import com.marknkamau.justjavastaff.R
import com.marknjunge.core.model.Order
import com.marknkamau.justjavastaff.ui.BaseActivity
import com.marknkamau.justjavastaff.ui.orderdetails.OrderDetailsActivity

import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : BaseActivity(), MainView {
    private lateinit var presenter: MainActivityPresenter
    private lateinit var adapter: OrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val settings = (application as JustJavaStaffApp).settingsRepository
        val databaseService = (application as JustJavaStaffApp).databaseService
        presenter = MainActivityPresenter(this, settings, databaseService)

        rvOrders.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = OrdersAdapter(this){order ->
            Timber.d(order.toString())
            goToDetails(order)
        }

        rvOrders.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        presenter.getOrders()
    }

    override fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun displayAvailableOrders(orders: MutableList<Order>) {
        pbLoading.visibility = View.GONE
        adapter.setItems(orders)
    }

    override fun displayNoOrders() {
        Toast.makeText(this, "There are no orders", Toast.LENGTH_SHORT).show()
        pbLoading.visibility = View.GONE
    }

    private fun goToDetails(order: Order) {
        val intent = Intent(this, OrderDetailsActivity::class.java)
        intent.putExtra(OrderDetailsActivity.ORDER, order)
        startActivity(intent)
    }
}
