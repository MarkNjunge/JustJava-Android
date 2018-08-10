package com.marknkamau.justjava.ui.previousOrder

import com.google.firebase.iid.FirebaseInstanceId
import com.marknkamau.justjava.data.models.Order
import com.marknkamau.justjava.data.models.OrderItem
import com.marknkamau.justjava.data.network.authentication.AuthenticationService
import com.marknkamau.justjava.data.network.db.DatabaseService
import com.marknkamau.justjava.ui.BasePresenter
import com.marknkamau.justjava.utils.mpesa.Mpesa
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class PreviousOrderPresenter(private val view: PreviousOrderView,
                             private val databaseService: DatabaseService,
                             private val mpesa: Mpesa,
                             private val authService: AuthenticationService)
    : BasePresenter() {

    fun getOrderDetails(orderId: String) {
        databaseService.getOrder(orderId, object : DatabaseService.OrderListener {
            override fun onSuccess(order: Order) {
                view.displayOrder(order)
            }

            override fun onError(reason: String) {
                Timber.e(reason)
                view.displayMessage(reason)
            }
        })
    }

    fun getOrderItems(orderId: String) {
        databaseService.getOrderItems(orderId, object : DatabaseService.OrderItemsListener {
            override fun onSuccess(items: List<OrderItem>) {
                view.displayOrderItems(items)
            }

            override fun onError(reason: String) {
                Timber.e(reason)
                view.displayMessage(reason)
            }
        })
    }

    fun makeMpesaPayment(total: Int, phoneNumber: String, orderId: String) {
        fun getFcmToken(): Single<String> {
            return Single.create<String> { emitter ->
                FirebaseInstanceId.getInstance().instanceId
                        .addOnSuccessListener { emitter.onSuccess(it.token) }
                        .addOnFailureListener {
                            Timber.e(it)
                            emitter.onError(it)
                        }
            }
        }

        getFcmToken().flatMap { token ->
            mpesa.sendStkPush(total, phoneNumber, orderId, token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { lnmPaymentResponse ->
                            view.displayMessage(lnmPaymentResponse.customerMessage)
                            if (lnmPaymentResponse.responseCode == "0") {
                                databaseService.savePaymentRequest(
                                        lnmPaymentResponse.merchantRequestId,
                                        lnmPaymentResponse.checkoutRequestId,
                                        orderId, authService.getUserId() ?: ""
                                )
                            }
                        },
                        { t ->
                            Timber.e(t)
                            view.displayMessage(t.message)
                        }
                )
    }
}