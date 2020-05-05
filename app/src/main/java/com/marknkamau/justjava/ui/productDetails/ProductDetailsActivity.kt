package com.marknkamau.justjava.ui.productDetails

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marknjunge.core.data.model.Product
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.models.AppProduct
import com.marknkamau.justjava.data.models.toAppModel
import com.marknkamau.justjava.ui.base.BaseActivity
import com.marknkamau.justjava.utils.CurrencyFormatter
import com.marknkamau.justjava.utils.replace
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.content_product_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDetailsActivity : BaseActivity() {

    companion object {
        private const val PRODUCT_KEY = "product_key"

        fun start(context: Context, product: Product, vararg sharedElements: Pair<View, String>) {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra(PRODUCT_KEY, product)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, *sharedElements)

            context.startActivity(intent, options.toBundle())
        }
    }

    private lateinit var appProduct: AppProduct
    private var quantity = 1
    private val productDetailsViewModel: ProductDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        val product = intent.extras!!.getParcelable(PRODUCT_KEY) as Product
        appProduct = product.toAppModel()

        setProductDetails()

        imgBackDetail.setOnClickListener { finish() }
        imgMinusQtyDetail.setOnClickListener { minusQty() }
        imgAddQtyDetail.setOnClickListener { addQty() }
        btnAddToCart.setOnClickListener { addToCart() }
    }

    private fun setProductDetails() {
        tvProductName.text = appProduct.name
        tvProductDescription.text = appProduct.description
        tvProductPrice.text = resources.getString(R.string.price_listing, CurrencyFormatter.format(appProduct.price))
        tvSubtotalDetail.text = resources.getString(R.string.price_listing, CurrencyFormatter.format(appProduct.price))
        tvQuantityDetail.text = quantity.toString()
        Picasso.get().load(appProduct.image).noFade().into(imgProductImage)

        appProduct.choices?.let { c ->
            // Use new object so that it is mutable
            var choices = c

            val choicesAdapter = ChoicesAdapter(this)
            rvProductChoices.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            rvProductChoices.adapter = choicesAdapter
            choicesAdapter.setItems(choices.sortedBy { it.id })

            choicesAdapter.onChoiceUpdated = { choice ->
                val existing = choices.first { it.id == choice.id }
                choices = choices.replace(existing, choice)

                appProduct.choices = choices
                updateTotal()
            }
        }

        updateTotal()
    }

    private fun addToCart() {
        val errors = appProduct.validate()

        if (errors.isNotEmpty()) {
            Toast.makeText(this, "You have not selected a choice for: ${errors.joinToString(", ")}", Toast.LENGTH_LONG)
                .show()
            return
        }

        productDetailsViewModel.addItemToCart(appProduct, quantity).observe(this, Observer {
            finish()
        })
    }

    private fun addQty() {
        quantity += 1
        tvQuantityDetail.text = quantity.toString()
        updateTotal()
    }

    private fun minusQty() {
        if (quantity == 1) return
        quantity -= 1
        tvQuantityDetail.text = quantity.toString()
        updateTotal()
    }

    private fun updateTotal() {
        val total = appProduct.calculateTotal(quantity)
        tvSubtotalDetail.text = getString(R.string.price_listing, CurrencyFormatter.format(total))
    }
}
