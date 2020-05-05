package com.marknkamau.justjava.data.network

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.marknjunge.core.data.model.OrderStatus
import com.marknjunge.core.data.model.Resource
import com.marknjunge.core.data.repository.UsersRepository
import com.marknkamau.justjava.data.models.NotificationReason
import com.marknkamau.justjava.utils.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent
import timber.log.Timber

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class JustJavaFirebaseMessagingService : FirebaseMessagingService(), KoinComponent {
    private val notificationHelper: NotificationHelper by inject()
    private val broadcastManager by lazy { LocalBroadcastManager.getInstance(this) }
    private val usersRepository: UsersRepository by inject()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    companion object {
        const val MPESA_ORDER_PAID_ACTION = "mpesa"
        const val ORDER_ID = "orderId"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            notificationHelper.showNotification(it.title ?: "JustJava", it.body ?: "")
        }

        remoteMessage.data?.let { data ->
            Timber.d(data.toString())
            val reason = data["reason"]
            if (reason == null) {
                Timber.e("Invalid message received")
                return@let
            }

            when (NotificationReason.valueOf(reason)) {
                NotificationReason.PAYMENT_COMPLETED -> {
                    // Show notification
                    notificationHelper.showPaymentNotification("Your payment has been received.")

                    val orderId = data["orderId"]
                    if (orderId == null) {
                        Timber.e("Notification received without orderId")
                        return@let
                    }
                    Timber.d("Payment received for order $orderId")

                    // Send local broadcast so that if the user is on the order's page, the status updates
                    val intent = Intent(MPESA_ORDER_PAID_ACTION)
                    intent.putExtra(ORDER_ID, orderId)
                    broadcastManager.sendBroadcast(intent)
                }
                NotificationReason.PAYMENT_CANCELLED -> {
                    // Show notification
                    notificationHelper.showPaymentNotification("Your payment was not successful. Please try again.")
                }
                NotificationReason.ORDER_STATUS_UPDATED -> {
                    val orderId = data["orderId"] as String
                    val orderStatus = OrderStatus.valueOf(data["orderStatus"] as String)
                    notificationHelper.showOrderStatusNotification(orderId, orderStatus)
                }
            }
        }
    }

    override fun onNewToken(token: String) {
        coroutineScope.launch {
            when (val resource = usersRepository.updateFcmToken()) {
                is Resource.Success -> Timber.d("FCM token updated")
                is Resource.Failure -> Timber.d("FCM token update failed: ${resource.response.message}")
            }
        }
    }
}
