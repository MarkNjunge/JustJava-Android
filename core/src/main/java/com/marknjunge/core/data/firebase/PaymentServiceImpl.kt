package com.marknjunge.core.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.marknjunge.core.model.DatabaseKeys
import com.marknjunge.core.model.Payment
import kotlinx.coroutines.tasks.await

internal class PaymentServiceImpl(private val firestore: FirebaseFirestore) : PaymentService {
    override suspend fun getPayments(): List<Payment> {

        val querySnapshot = firestore.collection("payments")
                .get()
                .await()

        return querySnapshot.mapNotNull { queryDocumentSnapshot -> queryDocumentSnapshot.maoToPayment() }
    }

    private fun QueryDocumentSnapshot.maoToPayment() = Payment(
            this[DatabaseKeys.Payment.checkoutRequestId] as String,
            this[DatabaseKeys.Payment.customerId] as String,
            this[DatabaseKeys.Payment.date] as Long,
            this[DatabaseKeys.Payment.merchantRequestId] as String,
            this[DatabaseKeys.Payment.orderId] as String,
            this[DatabaseKeys.Payment.resultDesc] as String,
            this[DatabaseKeys.Payment.status] as String,
            this[DatabaseKeys.Payment.mpesaReceiptNumber] as String?,
            (this[DatabaseKeys.Payment.phoneNumber] as Long?).toString()
    )

}