package com.marknkamau.justjava.ui.payCard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.base.BaseActivity
import com.marknkamau.justjava.utils.resetErrorOnChange
import com.marknkamau.justjava.utils.toast
import com.marknkamau.justjava.utils.trimmedText
import kotlinx.android.synthetic.main.activity_pay_card.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PayCardActivity : BaseActivity() {

    private val payCardViewModel: PayCardViewModel by viewModel()
    private lateinit var orderId: String
    override var requiresSignedIn = true

    companion object {
        private const val ORDER_ID_KEY = "order_id"

        fun start(context: Context, orderId: String) {
            val intent = Intent(context, PayCardActivity::class.java).apply {
                putExtra(ORDER_ID_KEY, orderId)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_card)
        supportActionBar?.title = "Pay for order"

        orderId = intent.extras!![ORDER_ID_KEY] as String

        observeLoading()

        til_payCard_cardNo.resetErrorOnChange(et_payCard_cardNo)
        til_payCard_expiry.resetErrorOnChange(et_payCard_expiry)
        til_payCard_cvv.resetErrorOnChange(et_payCard_cvv)
        btn_payCard_pay.setOnClickListener {
            if (valid()) {
                pay()
            }
        }
    }

    private fun observeLoading() {
        payCardViewModel.loading.observe(this, Observer { loading ->
            pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    private fun pay() {
        val expiryMonth = et_payCard_expiry.trimmedText.split("/")[0]
        val expiryYear = et_payCard_expiry.trimmedText.split("/")[1]

        btn_payCard_pay.isEnabled = false
        payCardViewModel.initiateCardPayment(
            orderId,
            et_payCard_cardNo.trimmedText,
            expiryMonth,
            expiryYear,
            et_payCard_cvv.trimmedText
        ).observe(this, Observer { resource ->
            btn_payCard_pay.isEnabled = true
            when (resource) {
                is Resource.Success -> {
                    toast("Payment successful")
                    finish()
                }
                is Resource.Failure -> handleApiError(resource)
            }
        })
    }

    private fun valid(): Boolean {
        var isValid = true

        if (et_payCard_cardNo.trimmedText.isEmpty()) {
            til_payCard_cardNo.error = "Required"
            isValid = false
        }

        if (et_payCard_expiry.trimmedText.isEmpty()) {
            til_payCard_expiry.error = "Required"
            isValid = false
        }

        if (et_payCard_cvv.trimmedText.isEmpty()) {
            til_payCard_cvv.error = "Required"
            isValid = false
        }

        return isValid
    }
}
