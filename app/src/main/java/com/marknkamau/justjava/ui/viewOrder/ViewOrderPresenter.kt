package com.marknkamau.justjava.ui.viewOrder

import com.marknjunge.core.auth.AuthService
import com.marknjunge.core.data.firebase.OrderService
import com.marknjunge.core.payments.PaymentsRepository
import com.marknkamau.justjava.ui.BasePresenter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class ViewOrderPresenter(private val view: ViewOrderView,
                         private val orderService: OrderService,
                         private val paymentsRepository: PaymentsRepository,
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
                paymentsRepository.makeLnmoRequest(total, phoneNumber, authService.getCurrentUser().userId, orderId)
                view.displayMessage("Success!")
            } catch (e: Exception) {
                Timber.e(e)
                view.displayMessage("Failed to initiate request")
            }
        }
    }

}