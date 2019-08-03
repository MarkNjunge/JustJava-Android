package com.marknkamau.justjava.testUtils.mocks

import com.marknkamau.justjava.utils.NotificationHelper
import io.mockk.mockk

object MockNotificationHelper {
    fun create() = mockk<NotificationHelper>()
}