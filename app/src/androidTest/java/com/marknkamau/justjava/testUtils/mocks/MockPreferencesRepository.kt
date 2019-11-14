package com.marknkamau.justjava.testUtils.mocks

import com.marknkamau.justjava.data.preferences.PreferencesRepository
import com.marknkamau.justjava.testUtils.TestData
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs

object MockPreferencesRepository {
    fun create(): PreferencesRepository {
        val mockPreferencesRepo = mockk<PreferencesRepository>()

        every { mockPreferencesRepo.clearUserDetails() } just runs
        every { mockPreferencesRepo.getUserDetails() } returns TestData.userDetails
        every { mockPreferencesRepo.saveUserDetails(any()) } just runs

        return mockPreferencesRepo
    }
}