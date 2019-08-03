package com.marknkamau.justjava.testUtils.mocks

import com.marknjunge.core.auth.AuthService
import com.marknkamau.justjava.testUtils.TestData
import io.mockk.*

object MockAuthService {
    fun create(): AuthService {
        val mockAuthService = mockk<AuthService>()

        every { mockAuthService.addStateListener(any()) } just runs
        coEvery { mockAuthService.createUser(any(), any()) } just runs
        coEvery { mockAuthService.signIn(any(), any()) } returns "OK"
        coEvery { mockAuthService.sendPasswordResetEmail(any()) } just runs
        coEvery { mockAuthService.setUserDisplayName(any()) } just runs
        every { mockAuthService.getCurrentUser() } returns TestData.authUser
        every { mockAuthService.isSignedIn() } returns true
        every { mockAuthService.logOut() } just runs

        return mockAuthService
    }
}