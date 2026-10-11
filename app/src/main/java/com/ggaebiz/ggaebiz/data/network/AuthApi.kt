package com.ggaebiz.ggaebiz.data.network

import com.ggaebiz.ggaebiz.data.network.dto.LoginRequest
import com.ggaebiz.ggaebiz.data.network.dto.LoginResponse
import com.ggaebiz.ggaebiz.data.network.dto.NicknameRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/social/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/users/nickname")
    suspend fun updateNickname(
        @Header("Authorization") authorization: String,
        @Body request: NicknameRequest,
    ): Response<Unit>

    @DELETE("api/users/withdraw")
    suspend fun withdraw(
        @Header("Authorization") authorization: String,
    ): Response<Unit>
}
