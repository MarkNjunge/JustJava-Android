package com.marknkamau.justjava.utils

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Date
import java.util.TimeZone

class DateTimeTest {

    @Before
    fun setup() {
        // Tests are configured based on GMT +3
        // Running on CI with a different timezone will result in errors
        TimeZone.setDefault(TimeZone.getTimeZone("Africa/Nairobi"))
    }

    @Test
    fun `converts from date`() {
        val dateTime = Date(1514784600L * 1000).toDateTime()

        Assert.assertEquals(2018, dateTime.year)
        Assert.assertEquals(1, dateTime.month)
        Assert.assertEquals(1, dateTime.dayOfMonth)
        Assert.assertEquals(8, dateTime.hourOfDay)
        Assert.assertEquals(30, dateTime.minute)
        Assert.assertEquals(0, dateTime.second)
    }

    @Test
    fun `converts to timestamp`() {
        val dateTime = DateTime(2018, 1, 1, 8, 30, 0)
        Assert.assertEquals(1514784600, dateTime.timestamp)
    }

    @Test
    fun `converts from timestamp`() {
        val dateTime = DateTime.fromTimestamp(1514784600)

        Assert.assertEquals(2018, dateTime.year)
        Assert.assertEquals(1, dateTime.month)
        Assert.assertEquals(1, dateTime.dayOfMonth)
        Assert.assertEquals(8, dateTime.hourOfDay)
        Assert.assertEquals(30, dateTime.minute)
        Assert.assertEquals(0, dateTime.second)
    }

    @Test
    fun `can format`() {
        val dateTime = DateTime(2001, 7, 4, 12, 8, 56)

        Assert.assertEquals("2001.07.04 AD at 12:08:56", dateTime.format("yyyy.MM.dd G 'at' HH:mm:ss"))
    }

    @Test
    fun `gets now`() {
        Assert.assertEquals(System.currentTimeMillis() / 1000, DateTime.now.timestamp)
    }
}