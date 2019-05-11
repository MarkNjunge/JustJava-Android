package com.marknjunge.core.utils

internal object Utils {
    fun sanitizePhoneNumber(phone: String): String {
        return when {
            phone.startsWith("0") -> phone.replaceFirst("^0".toRegex(), "254")
            phone.startsWith("+") -> phone.replaceFirst("^\\+".toRegex(), "")
            else -> phone
        }
    }
}
