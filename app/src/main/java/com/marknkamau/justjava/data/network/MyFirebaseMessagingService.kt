package com.marknkamau.justjava.data.network

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.marknkamau.justjava.JustJavaApp
import com.marknkamau.justjava.R

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val notificationHelper by lazy { (application as JustJavaApp).notificationHelper }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            val notification = notificationHelper.createNotification(
                    "Just java",
                    it.body ?: "Your order has been completed!",
                    getString(R.string.default_notification_channel)
            )

        notificationHelper.showNotification(1, notification)
        }
    }
}