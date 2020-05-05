package com.marknkamau.justjava.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * A class to be used to easily handle dates.
 * Months begin at 1
 */
data class DateTime(
    var year: Int,
    var month: Int,
    var dayOfMonth: Int,
    var hourOfDay: Int,
    var minute: Int,
    var second: Int = 0
) {

    companion object {
        /**
         * Returns the current time as a DateTime object.
         */
        val now: DateTime
            get() = Date(System.currentTimeMillis()).toDateTime()

        /**
         * Converts a timestamp(in seconds) to a DateTime object
         */
        fun fromTimestamp(timestamp: Long): DateTime = Date(timestamp * 1000).toDateTime()
    }

    /**
     * Time in seconds.
     */
    val timestamp: Long
        get() {
            val now = Calendar.getInstance()
            now.set(this.year, this.month - 1, this.dayOfMonth, this.hourOfDay, this.minute, this.second)
            return now.time.time / 1000L
        }

    /**
     * Formats the dateTime as the given format
     */
    @SuppressLint("SimpleDateFormat")
    fun format(format: String): String {
        val now = Calendar.getInstance()
        now.set(this.year, this.month - 1, this.dayOfMonth, this.hourOfDay, this.minute, this.second)
        return now.time.format(format)
    }

    @SuppressLint("SimpleDateFormat")
    fun parse(format: String, source: String): DateTime? = SimpleDateFormat(format).parse(source)?.toDateTime()
}

/**
 * Converts a java date to a DateTime object.
 */
fun Date.toDateTime(): DateTime {
    val hourOfDay = this.format("H").toInt() // Format according to 24Hr from 0-23
    val minute = this.format("m").toInt()
    val year = this.format("yyyy").toInt()
    val month = this.format("M").toInt()
    val dayOfMonth = this.format("dd").toInt()
    val second = this.format("s").toInt()

    return DateTime(year, month, dayOfMonth, hourOfDay, minute, second)
}

/**
 * Helper function to format dates.
 */
@SuppressLint("SimpleDateFormat")
fun Date.format(pattern: String): String = SimpleDateFormat(pattern).format(this)
