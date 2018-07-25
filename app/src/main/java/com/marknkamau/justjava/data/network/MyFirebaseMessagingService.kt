package com.marknkamau.justjava.data.network

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.marknkamau.justjava.JustJavaApp
import timber.log.Timber

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val notificationHelper by lazy { (application as JustJavaApp).notificationHelper }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            notificationHelper.showNotification(it.title ?: "JustJava", it.body ?: "")
        }
        remoteMessage.data?.let {
            Timber.d(it.toString())
            if (it["reason"] == "completed-order") {
                notificationHelper.showCompletedOrderNotification("Your order has been completed.")
            }

            when (it["reason"]) {
                "completed-order" -> {
                    notificationHelper.showCompletedOrderNotification("Your order has been completed.")
                }
                "mpesa" -> {
                    it["body"]?.let { body ->
                        notificationHelper.showNotification("Payment", body)
                    }
                }
                else -> {
                }
            }
        }
    }
}