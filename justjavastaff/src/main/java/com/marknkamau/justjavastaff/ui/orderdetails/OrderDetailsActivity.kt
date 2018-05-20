package com.marknkamau.justjavastaff.ui.orderdetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.marknkamau.justjavastaff.JustJavaStaffApp
import com.marknkamau.justjavastaff.R
import com.marknkamau.justjavastaff.models.Order
import com.marknkamau.justjavastaff.models.OrderItem
import com.marknkamau.justjavastaff.models.OrderStatus
import com.marknkamau.justjavastaff.models.User
import com.marknkamau.justjavastaff.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.include_customer_details.*
import kotlinx.android.synthetic.main.include_order_details.*

class OrderDetailsActivity : BaseActivity(), OrderDetailsView {
    companion object {
        val ORDER = "order"
    }

    private lateinit var currentStatus: OrderStatus
    private lateinit var nextStatus: OrderStatus
    private lateinit var presenter: OrderDetailsPresenter
    private lateinit var orderItemsAdapter: OrderItemsAdapter
    private lateinit var order: Order

    private val colorUtils by lazy { (application as JustJavaStaffApp).colorUtils }

//    private val colorPending by lazy { ContextCompat.getColor(this, R.color.colorPending) }
//    private val colorInProgress by lazy { ContextCompat.getColor(this, R.color.colorInProgress) }
//    private val colorCancelled by lazy { ContextCompat.getColor(this, R.color.colorCancelled) }
//    private val colorCompleted by lazy { ContextCompat.getColor(this, R.color.colorCompleted) }
//    private val colorDelivered by lazy { ContextCompat.getColor(this, R.color.colorDelivered) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        order = intent.getParcelableExtra(ORDER)

        presenter = OrderDetailsPresenter(this, (application as JustJavaStaffApp).dataRepository)
        presenter.getOrderItems(order.orderId)
        presenter.getUserDetails(order.customerId)

        rvOrderItems.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
//        rvOrderItems.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        orderItemsAdapter = OrderItemsAdapter()
        rvOrderItems.adapter = orderItemsAdapter

        tvOrderId.text = order.orderId
        tvOrderDate.text = order.date.toString()
        tvDeliveryAddress.text = order.deliveryAddress
        tvTotalPrice.text = getString(R.string.ksh, order.totalPrice)
        currentStatus = order.status

        if (order.additionalComments.isEmpty()) {
            tvCommentsLabel.visibility = View.GONE
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
    }

    override fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun displayOrderItems(items: MutableList<OrderItem>) {
        orderItemsAdapter.setItems(items)
    }

    override fun setUserDetails(user: User) {
        TransitionManager.beginDelayedTransition(sceneRoot)
        cardCustomerDetails.visibility = View.VISIBLE
        tvCustomerName.text = user.name
        tvCustomerPhone.text = user.phone
        imgCall.setOnClickListener {
            val i = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${user.phone}"))
            startActivity(i)
        }
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
                viewStatus.setBackgroundColor(colorUtils.colorPending)
                btnAdvanceOrder.setBackgroundColor(colorUtils.colorInProgress)
            }
            OrderStatus.INPROGRESS -> {
                viewStatus.setBackgroundColor(colorUtils.colorInProgress)
                btnAdvanceOrder.setBackgroundColor(colorUtils.colorCompleted)
            }
            OrderStatus.COMPLETED -> {
                viewStatus.setBackgroundColor(colorUtils.colorCompleted)
                btnAdvanceOrder.visibility = View.GONE
                btnCancelOrder.visibility = View.GONE
            }
            OrderStatus.CANCELLED -> {
                viewStatus.setBackgroundColor(colorUtils.colorCancelled)
                btnAdvanceOrder.visibility = View.GONE
                btnCancelOrder.visibility = View.GONE
            }
            OrderStatus.DELIVERED -> {
                viewStatus.setBackgroundColor(colorUtils.colorDelivered)
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

}
