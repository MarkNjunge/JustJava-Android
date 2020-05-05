package com.marknkamau.justjava.ui.orderDetail

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marknjunge.core.data.model.*
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.network.JustJavaFirebaseMessagingService
import com.marknkamau.justjava.ui.base.BaseActivity
import com.marknkamau.justjava.ui.payCard.PayCardActivity
import com.marknkamau.justjava.ui.payMpesa.PayMpesaActivity
import com.marknkamau.justjava.utils.BaseRecyclerViewAdapter
import com.marknkamau.justjava.utils.CurrencyFormatter
import com.marknkamau.justjava.utils.DateTime
import com.marknkamau.justjava.utils.toast
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.activity_order_detail.pbLoading
import kotlinx.android.synthetic.main.incl_order_details.*
import kotlinx.android.synthetic.main.item_order_item.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class OrderDetailActivity : BaseActivity() {

    private val orderDetailViewModel: OrderDetailViewModel by viewModel()
    private lateinit var order: Order
    private val broadcastManager by lazy { LocalBroadcastManager.getInstance(this) }
    private lateinit var broadcastReceiver: BroadcastReceiver
    override var requiresSignedIn = true

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

        btnChangePaymentMethod.setOnClickListener {
            showChangePaymentMethodDialog()
        }
        btnPayOrder.setOnClickListener {
            when (order.paymentMethod) {
                PaymentMethod.MPESA -> PayMpesaActivity.start(this, orderId)
                PaymentMethod.CARD -> PayCardActivity.start(this, orderId)
                else -> {
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == JustJavaFirebaseMessagingService.MPESA_ORDER_PAID_ACTION) {
                    val orderId = intent.getStringExtra(JustJavaFirebaseMessagingService.ORDER_ID)

                    if (orderId == orderId) {
                        Timber.d("The current order has been paid for!")
                        btnPayOrder.visibility = View.GONE
                        btnChangePaymentMethod.visibility = View.GONE
                        tv_order_paymentStatus.text = PaymentStatus.PAID.s
                        toast("Payment received")
                    }
                }
            }
        }

        broadcastManager.registerReceiver(
            broadcastReceiver,
            IntentFilter(JustJavaFirebaseMessagingService.MPESA_ORDER_PAID_ACTION)
        )
    }

    override fun onStop() {
        super.onStop()

        broadcastManager.unregisterReceiver(broadcastReceiver)
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
                is Resource.Failure -> handleApiError(resource)
            }
        })
    }

    @SuppressLint("DefaultLocale")
    private fun showChangePaymentMethodDialog() {
        val paymentMethods = PaymentMethod.values().map { it.s.toLowerCase().capitalize() }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle(R.string.payment_method)
            .setItems(paymentMethods) { _, which ->
                val paymentMethod = PaymentMethod.values()[which]
                changePaymentMethod(paymentMethod)
            }
            .create()
            .show()
    }

    private fun changePaymentMethod(paymentMethod: PaymentMethod) {
        orderDetailViewModel.changePaymentMethod(order.id, paymentMethod).observe(this, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    toast(resource.data.message)
                    order = order.copy(paymentMethod = paymentMethod)
                    displayOrderDetails(order)
                }
                is Resource.Failure -> handleApiError(resource)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun displayOrderDetails(order: Order) {
        this.order = order

        val address = orderDetailViewModel.getUser().address.firstOrNull { it.id == order.addressId }
        tv_order_orderId.text = order.id
        tv_order_orderStatus.text = order.status.s
        tv_order_orderDate.text = DateTime.fromTimestamp(order.datePlaced).format("hh:mm a, d MMM")
        tv_order_address.text = address?.streetAddress ?: "[Deleted address]"
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
            btnChangePaymentMethod.visibility = View.GONE
        }
        if (order.paymentStatus == PaymentStatus.PAID || order.paymentMethod == PaymentMethod.CASH) {
            btnPayOrder.visibility = View.GONE
        } else {
            btnPayOrder.visibility = View.VISIBLE
        }

        tv_order_total.text = getString(R.string.price_listing, CurrencyFormatter.format(order.totalPrice))
    }
}
