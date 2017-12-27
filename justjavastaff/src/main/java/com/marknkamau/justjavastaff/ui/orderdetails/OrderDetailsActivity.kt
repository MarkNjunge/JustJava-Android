package com.marknkamau.justjavastaff.ui.orderdetails

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import butterknife.ButterKnife
import com.marknkamau.justjavastaff.JustJavaStaffApp
import com.marknkamau.justjavastaff.R
import com.marknkamau.justjavastaff.models.Order
import com.marknkamau.justjavastaff.models.OrderItem
import com.marknkamau.justjavastaff.models.OrderStatus
import com.marknkamau.justjavastaff.ui.MenuBarActivity
import kotlinx.android.synthetic.main.activity_order_details.*

class OrderDetailsActivity : MenuBarActivity(), OrderDetailsView {
    companion object {
        val ORDER = "order"
    }

    private lateinit var currentStatus: OrderStatus
    private lateinit var nextStatus: OrderStatus
    private lateinit var presenter: OrderDetailsPresenter
    private lateinit var orderItemsAdapter: OrderItemsAdapter
    private lateinit var order: Order

    private var drawableInProgress: Drawable? = null
    private var drawableCompleted: Drawable? = null
    private var drawableDelivered: Drawable? = null

    private var colorPending: Int = 0
    private var colorInProgress: Int = 0
    private var colorCancelled: Int = 0
    private var colorCompleted: Int = 0
    private var colorDelivered: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        ButterKnife.bind(this)

        order = intent.getParcelableExtra(ORDER)

        colorPending = ContextCompat.getColor(this, R.color.colorPending)
        colorInProgress = ContextCompat.getColor(this, R.color.colorInProgress)
        colorCancelled = ContextCompat.getColor(this, R.color.colorCancelled)
        colorCompleted = ContextCompat.getColor(this, R.color.colorCompleted)
        colorDelivered = ContextCompat.getColor(this, R.color.colorDelivered)

        drawableInProgress = ContextCompat.getDrawable(this, R.drawable.button_in_progress)
        drawableCompleted = ContextCompat.getDrawable(this, R.drawable.button_completed)
        drawableDelivered = ContextCompat.getDrawable(this, R.drawable.button_delivered)

        rvOrderItems.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rvOrderItems.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        orderItemsAdapter = OrderItemsAdapter { _ -> }
        rvOrderItems.adapter = orderItemsAdapter

        tvOrderId.text = order.orderId
        tvOrderTime.text = order.timestamp.toString()
        tvName.text = order.customerName
        tvPhone.text = order.customerPhone
        tvAddress.text = order.deliveryAddress
        tvTotalPrice.text = order.totalPrice.toString()
        currentStatus = order.status
        if (order.additionalComments.isEmpty()) {
            tvComments.visibility = View.GONE
        } else {
            tvComments.text = order.additionalComments
        }

        updateNextStatus()
        setStatusView()

        btnAdvanceOrder.setOnClickListener {
            confirmAdvanceOrder()
        }

        btnCancelOrder.setOnClickListener {
            confirmCancelOrder()
        }

        presenter = OrderDetailsPresenter(this, (application as JustJavaStaffApp).ordersRepository)
        presenter.getOrderItems(order.orderId)
    }

    override fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    override fun displayOrderItems(items: MutableList<OrderItem>) {
        orderItemsAdapter.setItems(items)
    }

    override fun setOrderStatus(status: OrderStatus) {
        currentStatus = status
        setStatusView()
        updateNextStatus()
    }

    private fun updateNextStatus() {
        nextStatus = when (currentStatus) {
            OrderStatus.PENDING -> OrderStatus.INPROGRESS
            OrderStatus.INPROGRESS -> OrderStatus.COMPLETED
            else -> OrderStatus.COMPLETED
        }
    }

    private fun setStatusView() {
        when (currentStatus) {
            OrderStatus.PENDING -> {
                viewStatus.setBackgroundColor(colorPending)
                btnAdvanceOrder.background = drawableInProgress
            }
            OrderStatus.INPROGRESS -> {
                viewStatus.setBackgroundColor(colorInProgress)
                btnAdvanceOrder.background = drawableCompleted
            }
            OrderStatus.COMPLETED -> {
                viewStatus.setBackgroundColor(colorCompleted)
                btnAdvanceOrder.visibility = View.GONE
                btnCancelOrder.visibility = View.GONE
            }
            OrderStatus.CANCELLED -> {
                viewStatus.setBackgroundColor(colorCancelled)
                btnAdvanceOrder.visibility = View.GONE
                btnCancelOrder.visibility = View.GONE
            }
        }

    }

    private fun confirmAdvanceOrder() {
        val builder = AlertDialog.Builder(this)

        builder.setMessage("Are you sure you want to set the order to $nextStatus?")
                .setPositiveButton("Yes") { _, _ -> advanceOrder() }
                .setNegativeButton("No") { _, _ -> }
        builder.show()
    }

    private fun advanceOrder() {
        presenter.updateOrderStatus(order.orderId, nextStatus)
    }

    private fun confirmCancelOrder() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to cancel the order?")
                .setPositiveButton("Yes") { _, _ ->
                    presenter.updateOrderStatus(order.orderId, OrderStatus.CANCELLED)
                }
                .setNegativeButton("No") { _, _ -> }
        builder.show()
    }

//    private fun updateDatabaseStatus(status: String?) {
//        val orderStatusRef = databaseReference!!.child("allOrders/$orderID/currentStatus")
//        orderStatusRef.setValue(status).addOnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Toast.makeText(this@OrderDetailsActivity, "Error: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
//                currentStatus = previousStatus
//                refreshOrderStatus()
//            }
//        }
//
//        if (TextUtils.equals(status, COMPLETED)) {
//            val completedOrders = databaseReference!!.child("completedOrders")
//            completedOrders.push().setValue(orderID)
//        }
//        if (TextUtils.equals(status, CANCELLED)) {
//            val cancelledOrders = databaseReference!!.child("cancelledOrders")
//            cancelledOrders.push().setValue(orderID)
//        }
//    }


}
