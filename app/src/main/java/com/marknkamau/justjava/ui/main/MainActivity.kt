package com.marknkamau.justjava.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.model.Product
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.BaseActivity
import com.marknkamau.justjava.ui.productDetails.ProductDetailsActivity
import com.marknkamau.justjava.utils.BaseRecyclerViewAdapter
import com.marknkamau.justjava.utils.CurrencyFormatter
import com.marknkamau.justjava.utils.toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_toolbar.*
import kotlinx.android.synthetic.main.item_product.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initializeLoadingIndicator()
        initializeRecyclerView()

        viewModel.getProducts()
    }

    private fun initializeLoadingIndicator() {
        viewModel.loading.observe(this, Observer { loading ->
            TransitionManager.beginDelayedTransition(rootMainActivity)
            shimmerLayout.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    private fun initializeRecyclerView() {
        val adapter: BaseRecyclerViewAdapter<Product> = BaseRecyclerViewAdapter(R.layout.item_product) { product ->
            tvProductName.text = product.name
            tvProductPrice.text = context.resources.getString(R.string.price_listing, CurrencyFormatter.format(product.price))

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
            when(resource){
                is Resource.Success -> adapter.setItems(resource.data)
                is Resource.Failure -> toast(resource.message)
            }

        })
    }
}
