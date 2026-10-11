package com.ggaebiz.ggaebiz.data.repository

import android.content.Context
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.data.datastore.AuthDataStore
import com.ggaebiz.ggaebiz.data.network.AuthApi
import com.ggaebiz.ggaebiz.data.network.dto.NicknameRequest
import com.ggaebiz.ggaebiz.domain.repository.NicknameRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.firstOrNull

class NicknameRepositoryImpl(
    private val appContext: Context,
    private val authApi: AuthApi,
    private val authDataStore: AuthDataStore,
) : NicknameRepository {

    private val adjectives: List<String> by lazy { loadAdjectives() }

    override fun generateRandomNickname(): String {
        repeat(GENERATE_RETRY) {
            val adjective = adjectives.randomOrNull() ?: return@repeat
            val candidate = "$adjective ${CHARACTER_NAMES.random()}"
            if (candidate.length <= MAX_NICKNAME_LENGTH) return candidate
        }
        return CHARACTER_NAMES.random()
    }

    override suspend fun getNickname(): String? {
        return authDataStore.getNickname().firstOrNull()
    }

    override suspend fun updateNickname(nickname: String): Result<Unit> {
        return try {
            val token = authDataStore.getAccessToken().firstOrNull()
                ?: return Result.failure(IllegalStateException("로그인 정보가 없습니다."))

            val response = authApi.updateNickname(
                authorization = "Bearer $token",
                request = NicknameRequest(nickname = nickname),
            )

            if (response.isSuccessful) {
                authDataStore.saveNickname(nickname)
                Result.success(Unit)
            } else {
                Result.failure(IllegalStateException("닉네임 설정 실패 (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun loadAdjectives(): List<String> {
        return runCatching {
            appContext.resources.openRawResource(R.raw.nickname_adjectives)
                .bufferedReader()
                .use { it.readText() }
                .let { json ->
                    Gson().fromJson<List<String>>(
                        json,
                        object : TypeToken<List<String>>() {}.type,
                    )
                }
        }.getOrDefault(emptyList())
    }

    companion object {
        const val MAX_NICKNAME_LENGTH = 10
        private const val GENERATE_RETRY = 10
        private val CHARACTER_NAMES = listOf("키키", "나나", "부부", "보보", "차차")
    }
}
