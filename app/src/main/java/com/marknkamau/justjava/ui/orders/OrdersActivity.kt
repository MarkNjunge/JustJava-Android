package com.marknkamau.justjava.ui.orders

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.databinding.ActivityOrdersBinding
import com.marknkamau.justjava.ui.base.BaseActivity
import com.marknkamau.justjava.ui.orderDetail.OrderDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersActivity : BaseActivity() {

    private val ordersViewModel: OrdersViewModel by viewModels()
    override var requiresSignedIn = true
    private lateinit var binding: ActivityOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Orders"

        observeLoading()
        observeOrders()

        ordersViewModel.getOrders()
    }

    private fun observeLoading() {
        ordersViewModel.loading.observe(this, { loading ->
            binding.pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    @SuppressLint("DefaultLocale")
    private fun observeOrders() {
        val adapter = OrdersAdapter(this) { order ->
            OrderDetailActivity.start(this@OrdersActivity, order.id)
        }

        binding.rvOrders.stateListAnimator
        binding.rvOrders.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        binding.rvOrders.addItemDecoration(dividerItemDecoration)
        binding.rvOrders.adapter = adapter

        ordersViewModel.orders.observe(this, { resource ->
            when (resource) {
                is Resource.Success -> {
                    if (resource.data.isEmpty()) {
                        binding.llNoOrders.visibility = View.VISIBLE
                        binding.rvOrders.visibility = View.GONE
                    } else {
                        binding.llNoOrders.visibility = View.GONE
                        binding.rvOrders.visibility = View.VISIBLE
                        adapter.setItems(resource.data.sortedBy { it.datePlaced }.reversed())
                    }
                }
                is Resource.Failure -> handleApiError(resource)
            }
        })
    }
}
