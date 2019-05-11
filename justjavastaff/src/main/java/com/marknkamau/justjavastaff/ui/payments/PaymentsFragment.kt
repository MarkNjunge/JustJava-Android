package com.marknkamau.justjavastaff.ui.payments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marknkamau.justjavastaff.JustJavaStaffApp

import com.marknkamau.justjavastaff.R
import kotlinx.android.synthetic.main.fragment_payments.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class PaymentsFragment : Fragment() {

    private val uiScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_payments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPayments.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        val paymentsAdapter = PaymentsAdapter(requireContext()) {}
        rvPayments.adapter = paymentsAdapter

        val paymentService = (requireActivity().application as JustJavaStaffApp).paymentService

        uiScope.launch {
            try {
                val payments = paymentService.getPayments().sortedBy { it.date }
                paymentsAdapter.setItems(payments)
            } catch (e: Exception) {
                Timber.e(e)
                Toast.makeText(requireContext(), e.message
                        ?: "Error getting payments", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
