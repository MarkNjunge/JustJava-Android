package com.marknkamau.justjava.testUtils.mocks

import com.marknjunge.core.data.local.PreferencesRepository
import com.marknkamau.justjava.testUtils.TestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.runs

object MockPreferencesRepository {
    fun create(): PreferencesRepository {
        val mockPreferencesRepo = mockk<PreferencesRepository>()

        return mockPreferencesRepo
    }
}