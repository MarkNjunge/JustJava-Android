package com.marknkamau.justjava.ui.payCard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.databinding.ActivityPayCardBinding
import com.marknkamau.justjava.ui.base.BaseActivity
import com.marknkamau.justjava.utils.resetErrorOnChange
import com.marknkamau.justjava.utils.toast
import com.marknkamau.justjava.utils.trimmedText
import org.koin.androidx.viewmodel.ext.android.viewModel

class PayCardActivity : BaseActivity() {

    private val payCardViewModel: PayCardViewModel by viewModel()
    private lateinit var orderId: String
    override var requiresSignedIn = true
    private lateinit var binding: ActivityPayCardBinding

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
        binding = ActivityPayCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Pay for order"

        orderId = intent.extras!![ORDER_ID_KEY] as String

        observeLoading()

        binding.tilPayCardCardNo.resetErrorOnChange(binding.etPayCardCardNo)
        binding.tilPayCardExpiry.resetErrorOnChange(binding.etPayCardExpiry)
        binding.tilPayCardCvv.resetErrorOnChange(binding.etPayCardCvv)
        binding.btnPayCardPay.setOnClickListener {
            if (valid()) {
                pay()
            }
        }
    }

    private fun observeLoading() {
        payCardViewModel.loading.observe(this, { loading ->
            binding.pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    private fun pay() {
        val expiryMonth = binding.etPayCardExpiry.trimmedText.split("/")[0]
        val expiryYear = binding.etPayCardExpiry.trimmedText.split("/")[1]

        binding.btnPayCardPay.isEnabled = false
        payCardViewModel.initiateCardPayment(
            orderId,
            binding.etPayCardCardNo.trimmedText,
            expiryMonth,
            expiryYear,
            binding.etPayCardCvv.trimmedText
        ).observe(this, { resource ->
            binding.btnPayCardPay.isEnabled = true
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

        if (binding.etPayCardCardNo.trimmedText.isEmpty()) {
            binding.tilPayCardCardNo.error = "Required"
            isValid = false
        }

        if (binding.etPayCardExpiry.trimmedText.isEmpty()) {
            binding.tilPayCardExpiry.error = "Required"
            isValid = false
        }

        if (binding.etPayCardCvv.trimmedText.isEmpty()) {
            binding.tilPayCardCvv.error = "Required"
            isValid = false
        }

        return isValid
    }
}
