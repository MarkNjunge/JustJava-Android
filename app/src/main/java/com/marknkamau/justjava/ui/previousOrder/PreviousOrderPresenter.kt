package com.marknkamau.justjava.ui.previousOrder

import com.google.firebase.iid.FirebaseInstanceId
import com.marknjunge.core.mpesa.MpesaInteractor
import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.data.firebase.OrderService
import com.marknkamau.justjava.ui.BasePresenter
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import timber.log.Timber

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class PreviousOrderPresenter(private val view: PreviousOrderView,
                             private val orderService: OrderService,
                             private val mpesaInteractor: MpesaInteractor,
                             private val authService: AuthService,
                             mainDispatcher: CoroutineDispatcher
) : BasePresenter(mainDispatcher) {

    fun getOrderDetails(orderId: String) {
        uiScope.launch {
            try {
                val order = orderService.getOrder(orderId)

                view.displayOrder(order)
            } catch (e: Exception) {
                Timber.e(e)
                view.displayMessage(e.message)
            }
        }
    }

    fun getOrderItems(orderId: String) {
        uiScope.launch {
            try {
                val orderItems = orderService.getOrderItems(orderId)
                view.displayOrderItems(orderItems)
            } catch (e: Exception) {
                Timber.e(e)
                view.displayMessage(e.message)

            }
        }
    }

    fun makeMpesaPayment(total: Int, phoneNumber: String, orderId: String) {
        uiScope.launch {
            try {
                val fcmToken = FirebaseInstanceId.getInstance().instanceId.await().token

                mpesaInteractor.makeLnmoRequest(total, phoneNumber, authService.getCurrentUser().userId, orderId, fcmToken)
                view.displayMessage("Success!")
            } catch (e: Exception) {
                Timber.e(e)
                view.displayMessage("Failed to initiate request")
            }
        }
    }

}