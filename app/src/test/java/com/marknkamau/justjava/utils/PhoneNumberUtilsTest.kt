package com.marknkamau.justjava.utils

import org.junit.Assert
import org.junit.Test

class PhoneNumberUtilsKtTest {
    @Test
    fun `starts with zero`() {
        val number = "0712345678"
        val sanitized = PhoneNumberUtils.sanitize(number)
        Assert.assertEquals("254712345678", sanitized)
    }

    @Test
    fun `starts with plus`() {
        val number = "+254712345678"
        val sanitized = PhoneNumberUtils.sanitize(number)
        Assert.assertEquals("254712345678", sanitized)
    }

    @Test
    fun `can beautify`() {
        val number = "254712345678"
        val pretty = PhoneNumberUtils.beautify(number)
        Assert.assertEquals("+254 712 345 678", pretty)
    }
}