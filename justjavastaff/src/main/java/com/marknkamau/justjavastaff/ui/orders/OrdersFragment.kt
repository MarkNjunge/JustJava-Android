package com.marknkamau.justjavastaff.ui.orders

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.marknjunge.core.model.Order
import com.marknkamau.justjavastaff.JustJavaStaffApp

import com.marknkamau.justjavastaff.R
import com.marknkamau.justjavastaff.ui.orderdetails.OrderDetailsActivity
import kotlinx.android.synthetic.main.fragment_orders.*
import timber.log.Timber

class OrdersFragment : Fragment(), OrdersView {
    private lateinit var presenter: OrdersFragmentPresenter
    private lateinit var adapter: OrdersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val settings = (requireActivity().application as JustJavaStaffApp).settingsRepository
        val databaseService = (requireActivity().application as JustJavaStaffApp).databaseService
        presenter = OrdersFragmentPresenter(this, settings, databaseService)

        rvOrders.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter = OrdersAdapter(requireContext()) { order ->
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
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun displayAvailableOrders(orders: MutableList<Order>) {
        pbLoading.visibility = View.GONE
        adapter.setItems(orders)
    }

    override fun displayNoOrders() {
        Toast.makeText(requireContext(), "There are no orders", Toast.LENGTH_SHORT).show()
        pbLoading.visibility = View.GONE
    }

    private fun goToDetails(order: Order) {
        val intent = Intent(requireContext(), OrderDetailsActivity::class.java)
        intent.putExtra(OrderDetailsActivity.ORDER, order)
        startActivity(intent)
    }
}
