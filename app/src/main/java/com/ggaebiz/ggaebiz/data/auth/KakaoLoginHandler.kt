package com.ggaebiz.ggaebiz.data.auth

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

sealed interface KakaoLoginResult {
    data class Success(val accessToken: String) : KakaoLoginResult
    data class Failure(val message: String) : KakaoLoginResult
    data object Cancelled : KakaoLoginResult
}

class KakaoLoginHandler {
    suspend fun login(context: Context): KakaoLoginResult = suspendCoroutine { continuation ->
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            when {
                error != null -> continuation.resume(KakaoLoginResult.Failure(error.message ?: "로그인 실패"))
                token != null -> continuation.resume(KakaoLoginResult.Success(token.accessToken))
                else -> continuation.resume(KakaoLoginResult.Failure("알 수 없는 오류"))
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                when {
                    error != null -> {
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            continuation.resume(KakaoLoginResult.Cancelled)
                            return@loginWithKakaoTalk
                        }
                        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                    }
                    token != null -> continuation.resume(KakaoLoginResult.Success(token.accessToken))
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }
}
