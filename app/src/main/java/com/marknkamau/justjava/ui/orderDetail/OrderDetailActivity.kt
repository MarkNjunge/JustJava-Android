package com.marknkamau.justjava.ui.orderDetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marknjunge.core.data.model.Order
import com.marknjunge.core.data.model.OrderItem
import com.marknjunge.core.data.model.PaymentStatus
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.R
import com.marknkamau.justjava.utils.BaseRecyclerViewAdapter
import com.marknkamau.justjava.utils.CurrencyFormatter
import com.marknkamau.justjava.utils.DateTime
import com.marknkamau.justjava.utils.toast
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.incl_order_details.*
import kotlinx.android.synthetic.main.item_order_item.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderDetailActivity : AppCompatActivity() {

    private val orderDetailViewModel: OrderDetailViewModel by viewModel()
    private lateinit var order: Order

    companion object {
        const val ORDER_ID_KEY = "order_id"

        fun start(context: Context, orderId: String) {
            val intent = Intent(context, OrderDetailActivity::class.java).apply {
                putExtra(ORDER_ID_KEY, orderId)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        val orderId = intent.extras!![ORDER_ID_KEY] as String
        supportActionBar?.title = "Order - $orderId"

        observeLoading()
        observeOrder()

        orderDetailViewModel.getOrder(orderId)
    }

    private fun observeLoading() {
        orderDetailViewModel.loading.observe(this, Observer { loading ->
            pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    private fun observeOrder() {
        orderDetailViewModel.order.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    contentOrderDetails.visibility = View.VISIBLE
                    displayOrderDetails(resource.data)
                }
                is Resource.Failure -> toast(resource.message)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun displayOrderDetails(order: Order) {
        this.order = order

        val address = orderDetailViewModel.getUser().address.firstOrNull { it.id == order.addressId }
        tv_order_orderId.text = order.id
        tv_order_orderStatus.text = order.status
        tv_order_orderDate.text = DateTime.fromTimestamp(order.datePlaced).format("hh:mm a, d MMM")
        tv_order_address.text = address?.streetAddress ?: order.addressId.toString()
        if (order.additionalComments != null) {
            tv_order_additionalComments.text = order.additionalComments
        } else {
            tv_order_additionalComments.visibility = View.GONE
            tv_order_additionalCommentsTitle.visibility = View.GONE
        }

        val adapater = BaseRecyclerViewAdapter<OrderItem>(R.layout.item_order_item) { item ->
            tv_orderDetail_productName.text = item.productName
            tv_orderDetail_quantity.text = "${item.quantity}x"
            tv_orderDetail_price.text = getString(R.string.price_listing, CurrencyFormatter.format(item.totalPrice))
            tv_orderDetail_options.text = item.options.joinToString(", ") { it.optionName }
        }
        rv_order_items.layoutManager = LinearLayoutManager(this@OrderDetailActivity, RecyclerView.VERTICAL, false)
        rv_order_items.adapter = adapater
        adapater.setItems(order.items)

        tv_order_paymentMethod.text = order.paymentMethod.toString()
        tv_order_paymentStatus.text = order.paymentStatus.toString()
        if (order.paymentStatus == PaymentStatus.PAID) {
            btnPayOrder.visibility = View.GONE
        }

        tv_order_total.text = getString(R.string.price_listing, CurrencyFormatter.format(order.totalPrice))
    }
}
