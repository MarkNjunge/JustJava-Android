package com.marknkamau.justjavastaff.models

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */
data class StatusSettings(val pending: Boolean,
                          val inProgress: Boolean,
                          val completed: Boolean,
                          val delivered: Boolean,
                          val cancelled: Boolean)