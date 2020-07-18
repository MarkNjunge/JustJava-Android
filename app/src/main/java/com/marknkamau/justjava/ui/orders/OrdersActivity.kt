package com.marknkamau.justjava.ui.orders

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marknjunge.core.data.model.Order
import com.marknjunge.core.data.model.OrderStatus
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.base.BaseActivity
import com.marknkamau.justjava.ui.orderDetail.OrderDetailActivity
import com.marknkamau.justjava.utils.BaseRecyclerViewAdapter
import com.marknkamau.justjava.utils.CurrencyFormatter
import com.marknkamau.justjava.utils.DateTime
import kotlinx.android.synthetic.main.activity_orders.*
import kotlinx.android.synthetic.main.item_order.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrdersActivity : BaseActivity() {

    private val ordersViewModel: OrdersViewModel by viewModel()
    override var requiresSignedIn = true

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
            tvOrderId.text = order.id
            tvOrderDate.text = DateTime.fromTimestamp(order.datePlaced).format("hh:mm a, d MMM yyyy")
            tvOrderStatus.text = order.status.s.capitalize().replace("_", " ")
            tvOrderItems.text = order.items.joinToString { it.productName }
            tvOrderTotal.text = getString(R.string.price_listing, CurrencyFormatter.format(order.totalPrice))

            val statusTextColor = when (order.status) {
                OrderStatus.PENDING -> R.color.colorOrderPendingText
                OrderStatus.CONFIRMED -> R.color.colorOrderConfirmedText
                OrderStatus.IN_PROGRESS -> R.color.colorOrderInProgressText
                OrderStatus.COMPLETED -> R.color.colorOrderCompletedText
                OrderStatus.CANCELLED -> R.color.colorOrderCancelledText
            }
            tvOrderStatus.setTextColor(ContextCompat.getColor(context, statusTextColor))

            val statusDrawable = when (order.status) {
                OrderStatus.PENDING -> R.drawable.bg_order_pending
                OrderStatus.CONFIRMED -> R.drawable.bg_order_confirmed
                OrderStatus.IN_PROGRESS -> R.drawable.bg_order_in_progress
                OrderStatus.COMPLETED -> R.drawable.bg_order_completed
                OrderStatus.CANCELLED -> R.drawable.bg_order_cancelled
            }
            tvOrderStatus.setBackgroundResource(statusDrawable)

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
                is Resource.Success -> {
                    if (resource.data.isEmpty()) {
                        llNoOrders.visibility = View.VISIBLE
                        rvOrders.visibility = View.GONE
                    } else {
                        llNoOrders.visibility = View.GONE
                        rvOrders.visibility = View.VISIBLE
                        adapter.setItems(resource.data.sortedBy { it.datePlaced }.reversed())
                    }
                }
                is Resource.Failure -> handleApiError(resource)
            }
        })
    }
}
