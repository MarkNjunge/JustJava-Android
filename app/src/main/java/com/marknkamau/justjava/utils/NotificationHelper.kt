package com.marknkamau.justjava.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.marknkamau.justjava.R
import kotlin.random.Random

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class NotificationHelper(private val context: Context) {

    private val defaultChannelId = "defaultNotificationChannel"
    private val ordersChannelId = "ordersNotificationChannel"
    private val paymentsChannelId = "ordersNotificationChannel"

    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(defaultChannelId, "Default channel", "Notifications")
            createChannel(ordersChannelId, "Orders channel", "Notifications for orders")
            createChannel(paymentsChannelId, "Payments channel", "Notifications for payments")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(id: String, name: String, description: String) {
        val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)

        channel.description = description
        channel.enableLights(true)
        channel.enableVibration(true)

        notificationManager.createNotificationChannel(channel)
    }

    fun showCompletedOrderNotification() {
        showNotification("Completed Order", "Your order has been completed.", ordersChannelId)
    }

    fun showPaymentNotification(body: String) {
        showNotification("Order Payment", body, paymentsChannelId)
    }

    fun showNotification(title: String, body: String, channelId: String = defaultChannelId) {
        val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_just_java_logo_black)
                .setContentTitle(title)
                .setContentText(body)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}
