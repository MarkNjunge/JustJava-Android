package com.marknkamau.justjava.ui.payMpesa

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.databinding.ActivityPayMpesaBinding
import com.marknkamau.justjava.ui.base.BaseActivity
import com.marknkamau.justjava.utils.resetErrorOnChange
import com.marknkamau.justjava.utils.toast
import com.marknkamau.justjava.utils.trimmedText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PayMpesaActivity : BaseActivity() {

    private lateinit var orderId: String
    private val payMpesaViewModel: PayMpesaViewModel by viewModels()
    override var requiresSignedIn = true
    private lateinit var binding: ActivityPayMpesaBinding

    companion object {
        private const val ORDER_ID_KEY = "order_id"

        fun start(context: Context, orderId: String) {
            val intent = Intent(context, PayMpesaActivity::class.java).apply {
                putExtra(ORDER_ID_KEY, orderId)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayMpesaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Pay for order"

        orderId = intent.extras!![ORDER_ID_KEY] as String

        val user = payMpesaViewModel.getUser()
        user.mobileNumber?.let {
            binding.etPayMpesaMobilerNumber.setText(it)
        }

        observeLoading()

        binding.tilPayMpesaMobileNumber.resetErrorOnChange(binding.etPayMpesaMobilerNumber)
        binding.btnPayMpesaPay.setOnClickListener {
            if (valid()) {
                payMpesa()
            }
        }
    }

    private fun observeLoading() {
        payMpesaViewModel.loading.observe(this, { loading ->
            binding.pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    private fun payMpesa() {
        binding.btnPayMpesaPay.isEnabled = false
        payMpesaViewModel.payMpesa(binding.etPayMpesaMobilerNumber.trimmedText, orderId)
            .observe(this, { resource ->
                binding.btnPayMpesaPay.isEnabled = true
                when (resource) {
                    is Resource.Success -> {
                        toast("Request successful")
                        finish()
                    }
                    is Resource.Failure -> handleApiError(resource)
                }
            })
    }

    private fun valid(): Boolean {
        var isValid = true

        if (binding.etPayMpesaMobilerNumber.trimmedText.length != 12) {
            binding.tilPayMpesaMobileNumber.error = "Enter a valid mobile number"
            isValid = false
        }

        return isValid
    }
}
