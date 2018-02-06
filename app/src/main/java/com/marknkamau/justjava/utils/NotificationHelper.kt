package com.marknkamau.justjava.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.marknkamau.justjava.R

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class NotificationHelper(private val context: Context) {

    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createDefaultChannel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDefaultChannel() {
        val id = context.getString(R.string.default_notification_channel)
        val channel = NotificationChannel(id, "Default notification channel", NotificationManager.IMPORTANCE_DEFAULT)

        channel.description = "Default notifications"
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)

        notificationManager.createNotificationChannel(channel)
    }

    fun showCompletedOrderNotification(text: String) {
        val channelId = context.getString(R.string.default_notification_channel)
        val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_just_java_logo_black)
                .setContentTitle("Completed order")
                .setContentText(text)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .build()

        notificationManager.notify(1, notification)
    }
}
