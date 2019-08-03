package com.marknkamau.justjava.testUtils.mocks

import com.marknjunge.core.data.firebase.UserService
import com.marknkamau.justjava.testUtils.TestData
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs

object MockUserService {
    fun create(): UserService {
        val mock = mockk<UserService>()

        coEvery { mock.saveUserDetails(any()) } just runs
        coEvery { mock.updateUserDetails(any(), any(), any(), any()) } just runs
        coEvery { mock.updateUserFcmToken(any()) } just runs
        coEvery { mock.getUserDetails(any()) } returns TestData.userDetails

        return mock
    }
}