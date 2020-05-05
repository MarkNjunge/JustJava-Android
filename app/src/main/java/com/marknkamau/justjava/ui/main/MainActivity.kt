package com.marknkamau.justjava.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.marknjunge.core.data.model.Product
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.ToolbarActivity
import com.marknkamau.justjava.ui.productDetails.ProductDetailsActivity
import com.marknkamau.justjava.utils.BaseRecyclerViewAdapter
import com.marknkamau.justjava.utils.CurrencyFormatter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_product.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ToolbarActivity() {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeLoadingIndicator()
        initializeRecyclerView()
        observeSwipeToRefresh()

        viewModel.getProducts()
    }

    private fun initializeLoadingIndicator() {
        viewModel.loading.observe(this, Observer { loading ->
            TransitionManager.beginDelayedTransition(rootMainActivity)
            shimmerLayout.visibility = if (loading) View.VISIBLE else View.GONE
            if (loading) layoutFailed.visibility = View.GONE
        })
    }

    private fun initializeRecyclerView() {
        val adapter: BaseRecyclerViewAdapter<Product> = BaseRecyclerViewAdapter(R.layout.item_product) { product ->
            tvProductName.text = product.name
            tvProductPrice.text = resources.getString(R.string.price_listing, CurrencyFormatter.format(product.price))

            Picasso.get().load(product.image).placeholder(R.drawable.plain_brown).into(imgProductImage)

            productItem.setOnClickListener {
                ProductDetailsActivity.start(
                    this@MainActivity,
                    product,
                    Pair(imgProductImage, "productImage")
                )
            }
        }

        rvProducts.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.custom_item_divider)!!)
        rvProducts.addItemDecoration(dividerItemDecoration)
        rvProducts.adapter = adapter

        viewModel.products.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    TransitionManager.beginDelayedTransition(rootMainActivity)
                    rvProducts.visibility = View.VISIBLE
                    adapter.setItems(resource.data)
                }
                is Resource.Failure -> {
                    layoutFailed.visibility = View.VISIBLE
                    handleApiError(resource)
                }
            }
        })
    }

    private fun observeSwipeToRefresh() {
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
            viewModel.getProducts()
        }
    }
}
