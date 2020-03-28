package com.marknjunge.core.data.repository

import com.marknjunge.core.data.local.PreferencesRepository
import com.marknjunge.core.data.model.*
import com.marknjunge.core.data.model.SignInGoogleDto
import com.marknjunge.core.data.network.AuthService
import com.marknjunge.core.data.network.GoogleSignInClientStub
import com.marknjunge.core.utils.call
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String): Resource<User>

    suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Resource<User>

    suspend fun signIn(email: String, password: String): Resource<User>

    suspend fun signOut(): Resource<Unit>

    suspend fun requestPasswordReset(email: String): Resource<ApiResponse>

    suspend fun resetPassword(token: String, newPassword: String): Resource<ApiResponse>
}

internal class ApiAuthRepository(
    private val authService: AuthService,
    private val preferencesRepository: PreferencesRepository,
    private val googleSignInClient: GoogleSignInClientStub
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
        email: String,
        password: String
    ): Resource<User> = withContext(Dispatchers.IO) {
        call {
            val signUpDto = SignUpDto(firstName, lastName, password, email)
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

    override suspend fun signOut(): Resource<Unit> = withContext(Dispatchers.IO) {
        call {
            authService.signOut(preferencesRepository.sessionId)
            if (preferencesRepository.user!!.signInMethod == "GOOGLE") {
                googleSignInClient.signOut()
            }
            preferencesRepository.user = null
            preferencesRepository.sessionId = ""

            Resource.Success(Unit)
        }
    }

    override suspend fun requestPasswordReset(email: String) = withContext(Dispatchers.IO) {
        call {
            val response = authService.requestPasswordReset(RequestPasswordResetDto(email))
            Resource.Success(response)
        }
    }

    override suspend fun resetPassword(token: String, newPassword: String) = withContext(Dispatchers.IO) {
        call {
            val response = authService.resetPassword(ResetPasswordDto(token, newPassword))
            Resource.Success(response)
        }
    }
}