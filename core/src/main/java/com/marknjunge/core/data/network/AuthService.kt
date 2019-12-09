package com.marknjunge.core.data.network

import com.marknjunge.core.data.model.SignInDto
import com.marknjunge.core.data.model.SignInGoogleDto
import com.marknjunge.core.data.model.SignInResponse
import com.marknjunge.core.data.model.SignUpDto
import retrofit2.http.Body
import retrofit2.http.POST

internal interface AuthService {
    @POST("auth/google")
    suspend fun signInWithGoogle(@Body body: SignInGoogleDto): SignInResponse

    @POST("auth/signup")
    suspend fun signUp(@Body body: SignUpDto): SignInResponse

    @POST("auth/signin")
    suspend fun signIn(@Body body: SignInDto): SignInResponse

}