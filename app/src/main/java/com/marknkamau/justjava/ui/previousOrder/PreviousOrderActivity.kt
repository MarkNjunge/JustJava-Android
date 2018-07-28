package com.marknkamau.justjava.ui.previousOrder

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.models.Order
import com.marknkamau.justjava.data.models.OrderItem
import com.marknkamau.justjava.utils.formatForApp
import kotlinx.android.synthetic.main.activity_previous_order.*
import kotlinx.android.synthetic.main.include_order_details.*

class PreviousOrderActivity : AppCompatActivity(), PreviousOrderView {

    companion object {
        private const val ORDER_KEY = "order_key"
        fun start(context: Context, order: Order) {
            val i = Intent(context, PreviousOrderActivity::class.java)
            i.putExtra(ORDER_KEY, order)
            context.startActivity(i)
        }

    }

    private lateinit var orderItemsAdapter: OrderItemsAdapter
    private lateinit var presenter: PreviousOrderPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_order)

        val order = intent.getParcelableExtra<Order>(ORDER_KEY)

        val mpesa = (application as JustJavaApp).mpesa
        val preferencesRepo = (application as JustJavaApp).preferencesRepo
        val authService = (application as JustJavaApp).authService
        presenter = PreviousOrderPresenter(this, (application as JustJavaApp).databaseService, mpesa, authService)

        tvOrderId.text = order.orderId
        tvOrderStatus.text = order.status.name.toLowerCase().capitalize()
        tvOrderDate.text = order.date.formatForApp()
        tvDeliveryAddress.text = order.deliveryAddress
        tvTotalPrice.text = getString(R.string.ksh, order.totalPrice)
        if (order.additionalComments.isEmpty()) {
            tvComments.visibility = View.GONE
            tvCommentsLabel.visibility = View.GONE
        } else {
            tvComments.text = order.additionalComments
        }

        tvPaymentMethod.text = order.paymentMethod.capitalize()
        tvPaymentStatus.text = order.paymentStatus.capitalize()

        if (order.paymentMethod != "mpesa" || order.paymentStatus == "paid") {
            btnPay.visibility = View.GONE
        }

        btnPay.setOnClickListener {
            val phoneNumber = preferencesRepo.getUserDetails().phone
            val dialog = AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to pay Ksh. 1 using $phoneNumber?")
                    .setTitle("Confirm payment")
                    .setPositiveButton("Ok", { _, _ ->
                        presenter.makeMpesaPayment(1, phoneNumber, order.orderId)
                    })
                    .setNegativeButton("cancel", { dialogInterface, _ -> dialogInterface.dismiss() })
                    .create()

            dialog.show()
        }

        orderItemsAdapter = OrderItemsAdapter()
        rvOrderItems.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rvOrderItems.adapter = orderItemsAdapter

        presenter.getOrderItems(order.orderId)
    }

    override fun displayOrderItems(orderItems: List<OrderItem>) {
        cardOrderItems.visibility = View.VISIBLE
        orderItemsAdapter.setItems(orderItems)
    }

    override fun displayMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
