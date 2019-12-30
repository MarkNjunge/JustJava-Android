package com.marknkamau.justjava.ui.orders

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marknjunge.core.data.model.Order
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.orderDetail.OrderDetailActivity
import com.marknkamau.justjava.utils.BaseRecyclerViewAdapter
import com.marknkamau.justjava.utils.CurrencyFormatter
import com.marknkamau.justjava.utils.DateTime
import com.marknkamau.justjava.utils.toast
import kotlinx.android.synthetic.main.activity_orders.*
import kotlinx.android.synthetic.main.item_order.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrdersActivity : AppCompatActivity() {

    private val ordersViewModel: OrdersViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        supportActionBar?.title = "Orders"

        observeLoading()
        observeOrders()

        ordersViewModel.getOrders()
    }

    private fun observeLoading() {
        ordersViewModel.loading.observe(this, Observer { loading ->
            pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    @SuppressLint("DefaultLocale")
    private fun observeOrders() {
        val adapter: BaseRecyclerViewAdapter<Order> = BaseRecyclerViewAdapter(R.layout.item_order) { order ->
            tvOrderDate.text = DateTime.fromTimestamp(order.datePlaced).format("hh:mm a, d MMM")
            tvOrderItemsCount.text = resources.getQuantityString(R.plurals.item_s, order.items.size, order.items.size)
            tvOrderStatus.text = order.status.toLowerCase().capitalize().replace("_", " ")
            tvOrderItems.text = order.items.joinToString{ it.productName }
            tvOrderTotal.text = getString(R.string.price_listing, CurrencyFormatter.format(order.totalPrice))
            rootOrderItem.setOnClickListener {
                OrderDetailActivity.start(this@OrdersActivity, order.id)
            }
        }

        rvOrders.stateListAnimator
        rvOrders.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        rvOrders.addItemDecoration(dividerItemDecoration)
        rvOrders.adapter = adapter

        ordersViewModel.orders.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Success -> adapter.setItems(resource.data.sortedBy { it.datePlaced }.reversed())
                is Resource.Failure -> toast(resource.message, Toast.LENGTH_LONG)
            }
        })
    }
}
