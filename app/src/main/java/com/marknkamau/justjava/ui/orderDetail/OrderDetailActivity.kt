package com.marknkamau.justjava.ui.orderDetail

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marknjunge.core.data.model.*
import com.marknkamau.justjava.R
import com.marknkamau.justjava.data.network.JustJavaFirebaseMessagingService
import com.marknkamau.justjava.databinding.ActivityOrderDetailBinding
import com.marknkamau.justjava.ui.base.BaseActivity
import com.marknkamau.justjava.ui.payMpesa.PayMpesaActivity
import com.marknkamau.justjava.utils.*
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OrderDetailActivity : BaseActivity() {

    private val orderDetailViewModel: OrderDetailViewModel by viewModels()
    private lateinit var order: Order
    private val broadcastManager by lazy { LocalBroadcastManager.getInstance(this) }
    private lateinit var broadcastReceiver: BroadcastReceiver
    override var requiresSignedIn = true
    private lateinit var binding: ActivityOrderDetailBinding

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
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderId = intent.extras!![ORDER_ID_KEY] as String
        supportActionBar?.title = "Order - $orderId"

        observeLoading()
        observeOrder()

        orderDetailViewModel.getOrder(orderId)

        binding.btnChangePaymentMethod.setOnClickListener {
            showChangePaymentMethodDialog()
        }
        binding.btnPayOrder.setOnClickListener {
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
                        binding.btnPayOrder.visibility = View.GONE
                        binding.btnChangePaymentMethod.visibility = View.GONE
                        binding.tvOrderPaymentStatus.text = PaymentStatus.PAID.s
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
        orderDetailViewModel.loading.observe(this, { loading ->
            binding.pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    private fun observeOrder() {
        orderDetailViewModel.order.observe(this, { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.contentOrderDetails.visibility = View.VISIBLE
                    displayOrderDetails(resource.data)
                }
                is Resource.Failure -> handleApiError(resource)
            }
        })
    }

    @SuppressLint("DefaultLocale")
    private fun showChangePaymentMethodDialog() {
        val paymentMethods = PaymentMethod.values().map { it.s.capitalize() }.toTypedArray()
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
        orderDetailViewModel.changePaymentMethod(order.id, paymentMethod).observe(this, { resource ->
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
        binding.inclOrderDetails.tvOrderOrderId.text = order.id
        binding.inclOrderDetails.tvOrderOrderStatus.text = order.status.s
        binding.inclOrderDetails.tvOrderOrderDate.text =
            DateTime.fromTimestamp(order.datePlaced).format("hh:mm a, d MMM")
        binding.inclOrderDetails.tvOrderAddress.text = address?.streetAddress ?: "[Deleted address]"
        if (order.additionalComments != null) {
            binding.inclOrderDetails.tvOrderAdditionalComments.text = order.additionalComments
        } else {
            binding.inclOrderDetails.tvOrderAdditionalComments.visibility = View.GONE
            binding.inclOrderDetails.tvOrderAdditionalCommentsTitle.visibility = View.GONE
        }

        val adapter = OrderItemsAdapter(this)
        binding.rvOrderItems.layoutManager = LinearLayoutManager(this@OrderDetailActivity, RecyclerView.VERTICAL, false)
        binding.rvOrderItems.adapter = adapter
        adapter.setItems(order.items)

        binding.tvOrderPaymentMethod.text = order.paymentMethod.toString()
        binding.tvOrderPaymentStatus.text = order.paymentStatus.toString()
        if (order.paymentStatus == PaymentStatus.PAID) {
            binding.btnChangePaymentMethod.visibility = View.GONE
        }
        if (order.paymentStatus == PaymentStatus.PAID || order.paymentMethod == PaymentMethod.CASH) {
            binding.btnPayOrder.visibility = View.GONE
        } else {
            binding.btnPayOrder.visibility = View.VISIBLE
        }

        binding.tvOrderTotal.text = getString(R.string.price_listing, CurrencyFormatter.format(order.totalPrice))
    }
}
