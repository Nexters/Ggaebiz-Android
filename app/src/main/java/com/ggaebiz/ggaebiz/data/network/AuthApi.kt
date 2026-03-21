package com.ggaebiz.ggaebiz.data.network

import com.ggaebiz.ggaebiz.data.network.dto.LoginRequest
import com.ggaebiz.ggaebiz.data.network.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/social/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
