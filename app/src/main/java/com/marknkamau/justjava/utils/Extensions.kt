package com.marknkamau.justjava.utils

import android.annotation.SuppressLint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

@SuppressLint("SimpleDateFormat")
fun Date.formatForApp(): String {
    try {
        val simpleDateFormat = SimpleDateFormat("hh:mm a, d MMM")
        return simpleDateFormat.format(this)
    } catch (e: Exception) {
        Timber.e(e)
    }

    return this.toString()
}