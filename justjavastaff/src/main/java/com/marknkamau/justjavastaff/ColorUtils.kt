package com.marknkamau.justjavastaff

import android.content.Context
import androidx.core.content.ContextCompat

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class ColorUtils(context: Context) {
    val colorPending by lazy { ContextCompat.getColor(context, R.color.colorPending) }
    val colorInProgress by lazy { ContextCompat.getColor(context, R.color.colorInProgress) }
    val colorCancelled by lazy { ContextCompat.getColor(context, R.color.colorCancelled) }
    val colorCompleted by lazy { ContextCompat.getColor(context, R.color.colorCompleted) }
    val colorDelivered by lazy { ContextCompat.getColor(context, R.color.colorDelivered) }
}