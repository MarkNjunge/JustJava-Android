package com.marknkamau.justjava.ui.previousOrders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marknjunge.core.model.Order
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.previousOrder.PreviousOrderActivity
import com.marknkamau.justjava.utils.BaseRecyclerViewAdapter
import com.marknkamau.justjava.utils.DividerItemDecorator
import com.marknkamau.justjava.utils.formatForApp
import kotlinx.android.synthetic.main.activity_previous_orders.*
import kotlinx.android.synthetic.main.content_toolbar.*
import kotlinx.android.synthetic.main.item_previous_order.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class PreviousOrdersActivity : AppCompatActivity(), PreviousOrdersView {

    private lateinit var previousOrdersAdapter: BaseRecyclerViewAdapter<Order>
    private val presenter: PreviousOrdersPresenter by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_orders)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.previous_orders)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        previousOrdersAdapter = BaseRecyclerViewAdapter(R.layout.item_previous_order) { order ->
            tvOrderTimeItem.text = order.date.formatForApp()
            tvOrderStatusItem.text = order.status.name.toLowerCase().capitalize()
            tvOrderQtyItem.text = order.itemsCount.toString()
            tvOrderCountItem.text = resources.getQuantityString(R.plurals.order_info, order.itemsCount)
            tvOrderTotalItem.text = resources.getString(R.string.price_listing, order.totalPrice)

            previousOrderItemRootLayout.setOnClickListener {
                PreviousOrderActivity.start(this@PreviousOrdersActivity, order)
            }
        }

        rvPreviousOrdersActivity.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvPreviousOrdersActivity.addItemDecoration(DividerItemDecorator(getDrawable(R.drawable.custom_item_divider)!!))
        rvPreviousOrdersActivity.adapter = previousOrdersAdapter

        presenter.getPreviousOrders()
    }

    override fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun displayNoOrders() {
        pbLoadingOrdersPreviousActivity.visibility = View.GONE
        contentNoOrdersPreviousActivity.visibility = View.VISIBLE
        rvPreviousOrdersActivity.visibility = View.GONE
    }

    override fun displayOrders(orders: List<Order>) {
        pbLoadingOrdersPreviousActivity.visibility = View.GONE
        contentNoOrdersPreviousActivity.visibility = View.GONE
        rvPreviousOrdersActivity.visibility = View.VISIBLE
        previousOrdersAdapter.setItems(orders)
    }
}
