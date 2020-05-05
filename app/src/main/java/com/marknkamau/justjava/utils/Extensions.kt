package com.marknkamau.justjava.utils

import android.content.Context
import android.util.Patterns
import android.widget.Toast

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

fun <E> Iterable<E>.replace(old: E, new: E) = map { if (it == old) new else it }

fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Context.toast(text: String, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, text, duration).show()
