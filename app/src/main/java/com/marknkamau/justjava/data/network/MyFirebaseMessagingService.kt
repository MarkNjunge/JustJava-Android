package com.marknkamau.justjava.data.network

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.marknjunge.core.data.firebase.WriteListener
import com.marknkamau.justjava.JustJavaApp
import timber.log.Timber

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val notificationHelper by lazy { (application as JustJavaApp).notificationHelper }
    private val broadcastManager by lazy { (application as JustJavaApp).broadcastManager }
    private val authService by lazy { (application as JustJavaApp).authService }
    private val databaseService by lazy { (application as JustJavaApp).databaseService }

    companion object {
        const val MPESA_ORDER_PAID_ACTION = "mpesa"
        const val ORDER_ID = "orderId"
    }

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
                    it["status"]?.let { status ->
                        it["orderId"]?.let { orderId ->
                            if (status == "completed") {
                                val intent = Intent(MPESA_ORDER_PAID_ACTION)
                                intent.putExtra(ORDER_ID, orderId)
                                broadcastManager.sendBroadcast(intent)
                            }
                        }
                    }
                }
                else -> {
                }
            }
        }
    }

    override fun onNewToken(token: String) {
        if (authService.isSignedIn()){
            val user = authService.getCurrentUser()

            databaseService.updateUserFcmToken(user.userId, object : WriteListener {
                override fun onError(reason: String) {
                    Timber.e(reason)
                }

                override fun onSuccess() {
                    Timber.i("FCM token saved")
                }

            })
        }
    }
}