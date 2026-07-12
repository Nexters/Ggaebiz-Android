package com.ggaebiz.ggaebiz.data.repository

import com.ggaebiz.ggaebiz.data.datastore.AuthDataStore
import com.ggaebiz.ggaebiz.data.network.AuthApi
import com.ggaebiz.ggaebiz.data.network.dto.LoginRequest
import com.ggaebiz.ggaebiz.domain.repository.AuthRepository
import kotlinx.coroutines.flow.firstOrNull

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val authDataStore: AuthDataStore,
) : AuthRepository {

    override suspend fun login(kakaoAccessToken: String): Result<Unit> {
        return try {
            val request = LoginRequest(token = kakaoAccessToken)
            val response = authApi.login(request)
            authDataStore.saveAuthInfo(
                accessToken = response.accessToken,
                userId = response.userId,
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return authDataStore.isLoggedIn()
    }

    override suspend fun logout() {
        authDataStore.clearAuthInfo()
    }

    override suspend fun withdraw(): Result<Unit> {
        return try {
            val token = authDataStore.getAccessToken().firstOrNull()
                ?: return Result.failure(IllegalStateException("로그인 정보가 없습니다."))

            val response = authApi.withdraw(authorization = "Bearer $token")
            if (response.isSuccessful) {
                authDataStore.clearAuthInfo()
                Result.success(Unit)
            } else {
                Result.failure(IllegalStateException("회원 탈퇴 실패 (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAccessToken(): String? {
        return authDataStore.getAccessToken().firstOrNull()
    }

    override suspend fun getUserId(): Int? {
        return authDataStore.getUserId().firstOrNull()
    }
}
