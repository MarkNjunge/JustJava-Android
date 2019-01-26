package com.marknkamau.justjavastaff.ui.payments

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marknjunge.core.model.Payment
import com.marknkamau.justjavastaff.R
import kotlinx.android.synthetic.main.item_payment.view.*
import java.text.SimpleDateFormat
import java.util.*


class PaymentsAdapter(private val context: Context, private val onClick: (Payment) -> Unit) : androidx.recyclerview.widget.RecyclerView.Adapter<PaymentsAdapter.ViewHolder>() {

    private var data: List<Payment> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_payment, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, data[position], onClick)
    }

    fun setItems(data: List<Payment>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, payment: Payment, onClick: (Payment) -> Unit) {
            val dateFormat = SimpleDateFormat("hh:mm a, d MMM")

            itemView.run {
                tvOrderId.text = payment.orderId
                tvDate.text = dateFormat.format(Date(payment.date))
                if (payment.status == "completed") {
                    tvMpesaReceipt.visibility = View.VISIBLE
                    tvMpesaReceipt.text = payment.mpesaReceiptNumber
                    tvPhoneNumber.visibility = View.VISIBLE
                    tvPhoneNumber.text = "+${payment.phoneNumber}"
                }

                val color = when (payment.status) {
                    "failed" -> ContextCompat.getColor(context, R.color.colorCancelled)
                    "completed" -> ContextCompat.getColor(context, R.color.colorCompleted)
                    else -> ContextCompat.getColor(context, R.color.colorPending)
                }

                viewStatus.setBackgroundColor(color)
            }
        }
    }
}