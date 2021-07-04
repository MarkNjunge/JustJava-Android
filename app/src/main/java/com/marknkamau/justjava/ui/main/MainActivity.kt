package com.marknkamau.justjava.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.util.Pair
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.marknjunge.core.data.model.Product
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.R
import com.marknkamau.justjava.databinding.ActivityMainBinding
import com.marknkamau.justjava.ui.ToolbarActivity
import com.marknkamau.justjava.ui.productDetails.ProductDetailsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ToolbarActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeLoadingIndicator()
        initializeRecyclerView()
        observeSwipeToRefresh()

        viewModel.getProducts()
    }

    private fun initializeLoadingIndicator() {
        viewModel.loading.observe(this, { loading ->
            TransitionManager.beginDelayedTransition(binding.root)
            binding.shimmerLayout.visibility = if (loading) View.VISIBLE else View.GONE
            if (loading) binding.layoutFailed.visibility = View.GONE
        })
    }

    private fun initializeRecyclerView() {
        val adapter = ProductsAdapter { product: Product, view: View ->
            ProductDetailsActivity.start(
                this@MainActivity,
                product,
                Pair(view, "productImage")
            )
        }
        binding.rvProducts.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(AppCompatResources.getDrawable(this, R.drawable.custom_item_divider)!!)
        binding.rvProducts.addItemDecoration(dividerItemDecoration)
        binding.rvProducts.adapter = adapter

        viewModel.products.observe(this, { resource ->
            when (resource) {
                is Resource.Success -> {
                    TransitionManager.beginDelayedTransition(binding.root)
                    binding.rvProducts.visibility = View.VISIBLE
                    adapter.setItems(resource.data)
                }
                is Resource.Failure -> {
                    binding.layoutFailed.visibility = View.VISIBLE
                    handleApiError(resource)
                }
            }
        })
    }

    private fun observeSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            viewModel.getProducts()
        }
    }
}
