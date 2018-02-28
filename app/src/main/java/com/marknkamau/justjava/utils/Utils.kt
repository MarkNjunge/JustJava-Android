package com.marknkamau.justjava.utils

import android.util.Base64

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    val timestampNow: String
        get() = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())

    fun sanitizePhoneNumber(phone: String): String {
        return when {
            phone.startsWith("0") -> phone.replaceFirst("^0".toRegex(), "254")
            phone.startsWith("+") -> phone.replaceFirst("^+".toRegex(), "")
            else -> phone
        }
    }

    fun getPassword(businessShortCode: String, passkey: String, timestamp: String): String {
        val str = businessShortCode + passkey + timestamp
        return Base64.encodeToString(str.toByteArray(), Base64.NO_WRAP)
    }
}
