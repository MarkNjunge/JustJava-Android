package com.marknkamau.justjava.utils

import io.sentry.Breadcrumb
import io.sentry.Sentry
import io.sentry.SentryLevel
import timber.log.Timber

class ReleaseTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val breadcrumb = Breadcrumb().apply {
            this.message = message
            this.level = when (priority) {
                3 -> SentryLevel.DEBUG
                4 -> SentryLevel.INFO
                5 -> SentryLevel.WARNING
                6 -> SentryLevel.ERROR
                else -> SentryLevel.INFO
            }
        }
        Sentry.addBreadcrumb(breadcrumb)
    }
}
