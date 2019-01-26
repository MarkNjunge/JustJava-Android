package com.marknkamau.justjavastaff.ui.payments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.marknjunge.core.data.firebase.StaffDatabaseService
import com.marknjunge.core.model.Payment
import com.marknkamau.justjavastaff.JustJavaStaffApp

import com.marknkamau.justjavastaff.R
import kotlinx.android.synthetic.main.fragment_payments.*
import timber.log.Timber

class PaymentsFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_payments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPayments.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
        val paymentsAdapter = PaymentsAdapter(requireContext()) {}
        rvPayments.adapter = paymentsAdapter

        val databaseService = (requireActivity().application as JustJavaStaffApp).databaseService
        databaseService.getPayments(object : StaffDatabaseService.PaymentsListener {
            override fun onSuccess(payments: List<Payment>) {
                Timber.d(payments.toString())
                paymentsAdapter.setItems(payments)
            }

            override fun onError(reason: String) {
                Timber.e(reason)
            }

        })
    }

}
