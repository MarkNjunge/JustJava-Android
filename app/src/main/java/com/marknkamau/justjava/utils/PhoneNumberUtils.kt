package com.marknkamau.justjava.utils

object PhoneNumberUtils {
    fun sanitize(number: String): String {
        val num = number.replace(" ", "")
        return when {
            num.startsWith("0") -> num.replaceFirst("^0".toRegex(), "254")
            num.startsWith("+") -> num.replaceFirst("^\\+".toRegex(), "")
            else -> number
        }
    }

    fun beautify(number: String): String {
        val num = sanitize(number)
        return "+${num.subSequence(0, 3)} ${num.subSequence(3, 6)} ${num.subSequence(6, 9)} ${num.subSequence(9, 12)}"
    }
}
