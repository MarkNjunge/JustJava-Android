package com.marknkamau.justjava.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Patterns
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up.*
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

fun <E> Iterable<E>.replace(old: E, new: E) = map { if (it == old) new else it }

fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Context.toast(text: String, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, text, duration).show()