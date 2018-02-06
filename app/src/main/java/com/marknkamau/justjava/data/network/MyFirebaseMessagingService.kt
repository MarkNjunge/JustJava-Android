package com.marknkamau.justjava.data.network

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.marknkamau.justjava.JustJavaApp

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val notificationHelper by lazy { (application as JustJavaApp).notificationHelper }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.data?.let {
            if(it["reason"] == "completed-order"){
                notificationHelper.showCompletedOrderNotification("Your order has been completed.")
            }
        }
    }
}