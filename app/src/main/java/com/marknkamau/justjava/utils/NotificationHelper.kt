package com.marknkamau.justjava.utils

import android.app.*
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel(id: String,
                      name: String,
                      description: String,
                      importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
                      groupId: String? = null
    ): NotificationChannel {
        val channel = NotificationChannel(id, name, importance)

        channel.description = description
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)

        if (groupId != null) {
            channel.group = groupId
        }

        notificationManager.createNotificationChannel(channel)

        return channel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteChannel(channel: NotificationChannel) {
        notificationManager.deleteNotificationChannel(channel.id)
    }

    fun createNotification(title: String,
                           text: String,
                           channelId: String,
                           iconId: Int = R.drawable.ic_just_java_logo_black,
                           intent: PendingIntent? = null,
                           style: NotificationCompat.Style? = null)
            : Notification {

        val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(iconId)
                .setContentTitle(title)
                .setContentText(text)

        intent?.let {
            builder.setContentIntent(intent)
        }
        style?.let {
            builder.setStyle(style)
        }
        builder.color = ContextCompat.getColor(context, R.color.colorAccent)

        return builder.build()
    }

    fun showNotification(notificationId: Int, notification: Notification) {
        notificationManager.notify(notificationId, notification)
    }

    fun clearNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }
}
