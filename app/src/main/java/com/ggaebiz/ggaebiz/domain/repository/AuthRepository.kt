package com.ggaebiz.ggaebiz.domain.repository

interface AuthRepository {
    suspend fun login(kakaoAccessToken: String): Result<Unit>
    suspend fun isLoggedIn(): Boolean
    suspend fun logout()
    suspend fun getAccessToken(): String?
    suspend fun getUserId(): Int?
}
