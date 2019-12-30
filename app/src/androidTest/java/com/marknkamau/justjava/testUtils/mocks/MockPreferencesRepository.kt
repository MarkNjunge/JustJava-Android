package com.marknkamau.justjava.testUtils.mocks

import com.marknjunge.core.data.local.PreferencesRepository
import io.mockk.mockk

object MockPreferencesRepository {
    fun create(): PreferencesRepository {
        val mockPreferencesRepo = mockk<PreferencesRepository>()

        return mockPreferencesRepo
    }
}