package com.marknjunge.core.data.network.service

import com.marknjunge.core.data.model.*
import com.marknjunge.core.data.model.SignInGoogleDto
import com.marknjunge.core.data.model.SignInResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

internal interface AuthService {
    @POST("auth/google")
    suspend fun signInWithGoogle(@Body body: SignInGoogleDto): SignInResponse

    @POST("auth/signup")
    suspend fun signUp(@Body body: SignUpDto): SignInResponse

    @POST("auth/signin")
    suspend fun signIn(@Body body: SignInDto): SignInResponse

    @DELETE("auth/signout")
    suspend fun signOut()

    @POST("auth/requestPasswordReset")
    suspend fun requestPasswordReset(@Body body: RequestPasswordResetDto): ApiResponse

    @POST("auth/resetPassword")
    suspend fun resetPassword(@Body body: ResetPasswordDto): ApiResponse
}
