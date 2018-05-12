package com.marknkamau.justjava.utils

import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class UtilsTest {

    @Test
    fun should_sanitizePhoneNumber() {
        val num1 = "0712345678"
        val num1Sanitized = Utils.sanitizePhoneNumber(num1)
        Assert.assertEquals("254712345678", num1Sanitized)

        val num2 = "+254712345678"
        val num2Sanitized = Utils.sanitizePhoneNumber(num2)
        Assert.assertEquals("254712345678", num2Sanitized)
    }
}