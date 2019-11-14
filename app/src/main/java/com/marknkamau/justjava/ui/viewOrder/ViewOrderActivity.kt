package com.marknkamau.justjava.ui.viewOrder

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.marknjunge.core.model.Order
import com.marknjunge.core.model.OrderItem
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.preferences.PreferencesRepository
import com.marknkamau.justjava.data.network.MyFirebaseMessagingService
import com.marknkamau.justjava.utils.BaseRecyclerViewAdapter
import com.marknkamau.justjava.utils.formatForApp
import kotlinx.android.synthetic.main.activity_view_order.*
import kotlinx.android.synthetic.main.content_toolbar.*
import kotlinx.android.synthetic.main.item_order_item.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class ViewOrderActivity : AppCompatActivity(), ViewOrderView {

    companion object {
        const val ORDER_KEY = "order_key"
        fun start(context: Context, order: Order) {
            val i = Intent(context, ViewOrderActivity::class.java)
            i.putExtra(ORDER_KEY, order)
            context.startActivity(i)
        }

    }

    private lateinit var orderItemsAdapter: BaseRecyclerViewAdapter<OrderItem>
    private lateinit var broadcastReceiver: BroadcastReceiver
    private lateinit var order: Order

    private val preferencesRepository: PreferencesRepository by inject()
    private val broadcastManager by lazy { LocalBroadcastManager.getInstance(this) }
    private val presenter: ViewOrderPresenter by inject { parametersOf(this) }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_order)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Order"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        order = intent.getParcelableExtra(ORDER_KEY)

        updateViews(order)

        orderItemsAdapter = BaseRecyclerViewAdapter(R.layout.item_order_item){orderItem ->
            tvItemNameItem.text = orderItem.itemName
            tvItemQtyItem.text = "${orderItem.itemQty}x"
            tvItemPriceItem.text = context.getString(R.string.price_listing, orderItem.itemPrice)

            val toppings = mutableListOf<String>()

            if (orderItem.itemCinnamon) toppings.add("Cinnamon")
            if (orderItem.itemChoc) toppings.add("Chocolate")
            if (orderItem.itemMarshmallow) toppings.add("Marshmallows")

            if (toppings.isNotEmpty()) {
                tvToppingsItem.visibility = View.VISIBLE
                tvToppingsItem.text = toppings.joinToString(", ")
            } else {
                tvToppingsItem.visibility = View.GONE
            }
        }

        rvOrderItemsOrder.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvOrderItemsOrder.adapter = orderItemsAdapter

        presenter.getOrderItems(order.orderId)
    }

    override fun onStart() {
        super.onStart()

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == MyFirebaseMessagingService.MPESA_ORDER_PAID_ACTION) {
                    val orderId = intent.getStringExtra(MyFirebaseMessagingService.ORDER_ID)
                    Timber.d("Payment received for order $orderId")
                    if (order.orderId == orderId) {
                        Timber.d("The current order has been paid for!")
                        btnPayOrder.visibility = View.GONE
                        tvPaymentStatusOrder .text = getString(R.string.paid)
                        displayMessage("Payment received!")
                    }
                }
            }
        }

        broadcastManager.registerReceiver(broadcastReceiver, IntentFilter(MyFirebaseMessagingService.MPESA_ORDER_PAID_ACTION))
    }

    override fun onResume() {
        super.onResume()
        presenter.getOrderDetails(order.orderId)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.cancel()
        broadcastManager.unregisterReceiver(broadcastReceiver)
    }

    override fun displayOrderItems(orderItems: List<OrderItem>) {
        pbLoadingOrderItemsOrder.visibility = View.GONE
        rvOrderItemsOrder.visibility = View.VISIBLE
        orderItemsAdapter.setItems(orderItems)
    }

    override fun displayMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun displayOrder(order: Order) {
        this.order = order
        updateViews(order)
    }

    private fun updateViews(order: Order) {
        tvOrderIdOrder.text = order.orderId
        tvOrderStatusOrder.text = order.status.name.toLowerCase().capitalize()
        tvOrderDateOrder.text = order.date.formatForApp()
        tvOrderAddressOrder.text = order.deliveryAddress
        tvOrderTotalOrder .text = getString(R.string.ksh, order.totalPrice)
        if (order.additionalComments.isEmpty()) {
            tvOrderCommentsOrder.visibility = View.GONE
            tvCommentsLabelOrder.visibility = View.GONE
        } else {
            tvOrderCommentsOrder.text = order.additionalComments
        }

        tvPaymentMethodOrder.text = order.paymentMethod.capitalize()
        tvPaymentStatusOrder.text = order.paymentStatus.capitalize()

        if (order.paymentMethod != "mpesa" || order.paymentStatus == "paid") {
            btnPayOrder.visibility = View.GONE
        }

        btnPayOrder.setOnClickListener {
            val phoneNumber = preferencesRepository.getUserDetails().phone
            val dialog = AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to pay Ksh. 1 using $phoneNumber?\nThe money will be automatically refunded by Safaricom the following day.")
                    .setTitle("Confirm payment")
                    .setPositiveButton("Ok") { _, _ ->
                        presenter.makeMpesaPayment(1, phoneNumber, order.orderId)
                    }
                    .setNegativeButton("cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
                    .create()

            dialog.show()
        }
    }
}
