package com.marknkamau.justjava.ui.payMpesa

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.marknjunge.core.data.model.Resource
import com.marknkamau.justjava.R
import com.marknkamau.justjava.utils.resetErrorOnChange
import com.marknkamau.justjava.utils.toast
import com.marknkamau.justjava.utils.trimmedText
import kotlinx.android.synthetic.main.activity_pay_mpesa.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PayMpesaActivity : AppCompatActivity() {

    private lateinit var orderId: String
    private val payMpesaViewModel: PayMpesaViewModel by viewModel()

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
        setContentView(R.layout.activity_pay_mpesa)
        supportActionBar?.title = "Pay for order"

        orderId = intent.extras!![ORDER_ID_KEY] as String

        val user = payMpesaViewModel.getUser()
        et_payMpesa_mobilerNumber.setText(user.mobileNumber)

        observeLoading()

        til_payMpesa_mobileNumber.resetErrorOnChange(et_payMpesa_mobilerNumber)
        btn_payMpesa_pay.setOnClickListener {
            if (valid()) {
                payMpesa()
            }
        }
    }

    private fun observeLoading() {
        payMpesaViewModel.loading.observe(this, Observer { loading ->
            pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        })
    }

    private fun payMpesa() {
        btn_payMpesa_pay.isEnabled = false
        payMpesaViewModel.payMpesa(et_payMpesa_mobilerNumber.trimmedText, orderId).observe(this, Observer { resource ->
            btn_payMpesa_pay.isEnabled = true
            when (resource) {
                is Resource.Success -> {
                    toast("Request successful")
                    finish()
                }
                is Resource.Failure -> toast(resource.message)
            }
        })
    }

    private fun valid(): Boolean {
        var isValid = true

        if (et_payMpesa_mobilerNumber.trimmedText.length != 12) {
            til_payMpesa_mobileNumber.error = "Enter a valid mobile number"
            isValid = false
        }

        return isValid
    }
}
