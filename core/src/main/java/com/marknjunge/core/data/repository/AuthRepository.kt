package com.marknjunge.core.data.repository

import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.*
import com.marknjunge.core.data.model.SignInGoogleDto
import com.marknjunge.core.data.network.AuthService
import com.marknjunge.core.utils.call
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String): Resource<User>

    suspend fun signUp(
        firstName: String,
        lastName: String,
        mobile: String,
        email: String,
        password: String
    ): Resource<User>

    suspend fun signIn(email: String, password: String): Resource<User>
}

internal class ApiAuthRepository(
    private val authService: AuthService,
    private val preferencesRepository: PreferencesRepository
) : AuthRepository {
    override suspend fun signInWithGoogle(idToken: String): Resource<User> = withContext(Dispatchers.IO) {
        call {
            val response = authService.signInWithGoogle(SignInGoogleDto(idToken))

            preferencesRepository.user = response.user
            preferencesRepository.sessionId = response.session.sessionId

            Resource.Success(response.user)
        }
    }

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        mobile: String,
        email: String,
        password: String
    ): Resource<User> = withContext(Dispatchers.IO) {
        call {
            val signUpDto = SignUpDto(firstName, lastName, password, mobile, email)
            val response = authService.signUp(signUpDto)

            preferencesRepository.user = response.user
            preferencesRepository.sessionId = response.session.sessionId

            Resource.Success(response.user)
        }
    }

    override suspend fun signIn(email: String, password: String): Resource<User> = withContext(Dispatchers.IO) {
        call {
            val response = authService.signIn(SignInDto(email, password))

            preferencesRepository.user = response.user
            preferencesRepository.sessionId = response.session.sessionId

            Resource.Success(response.user)
        }
    }
}