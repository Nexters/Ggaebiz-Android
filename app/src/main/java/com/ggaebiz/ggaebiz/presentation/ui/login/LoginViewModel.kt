package com.ggaebiz.ggaebiz.presentation.ui.login

import com.ggaebiz.ggaebiz.data.auth.KakaoLoginResult
import com.ggaebiz.ggaebiz.domain.repository.AuthRepository
import com.ggaebiz.ggaebiz.domain.repository.NicknameRepository
import com.ggaebiz.ggaebiz.domain.repository.OnboardingRepository
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel

data class LoginState(
    val isOnboardingExposed: Boolean = false,
    val isLoading: Boolean = false,
)

sealed interface LoginSideEffect {
    data object NavigateOnboarding : LoginSideEffect
    data object NavigateHome : LoginSideEffect
    data object RequestKakaoLogin : LoginSideEffect
    data class ShowToast(val message: String) : LoginSideEffect
}

sealed interface LoginIntent {
    data object ClickLoginButton : LoginIntent
    data class OnLoginResult(val result: KakaoLoginResult) : LoginIntent
}

class LoginViewModel(
    private val onboardingRepository: OnboardingRepository,
    private val authRepository: AuthRepository,
    private val nicknameRepository: NicknameRepository,
) : BaseViewModel<LoginState, LoginIntent, LoginSideEffect>(LoginState()) {

    init {
        checkOnboardingExposed()
    }

    private fun checkOnboardingExposed() = launch {
        val status = onboardingRepository.getIsOnboardingExposed()
        updateState { it.copy(isOnboardingExposed = status) }
    }

    fun processIntent(intent: LoginIntent) {
        when (intent) {
            LoginIntent.ClickLoginButton -> postSideEffect(LoginSideEffect.RequestKakaoLogin)
            is LoginIntent.OnLoginResult -> handleLoginResult(intent.result)
        }
    }

    private fun handleLoginResult(result: KakaoLoginResult) {
        when (result) {
            is KakaoLoginResult.Success -> {
                loginToServer(result.accessToken)
            }
            is KakaoLoginResult.Failure -> {
                postSideEffect(LoginSideEffect.ShowToast(result.message))
            }
            KakaoLoginResult.Cancelled -> Unit
        }
    }

    private fun loginToServer(kakaoAccessToken: String) = launch {
        updateState { it.copy(isLoading = true) }

        authRepository.login(kakaoAccessToken)
            .onSuccess {
                assignRandomNickname()
            }
            .onFailure { error ->
                updateState { it.copy(isLoading = false) }
                postSideEffect(LoginSideEffect.ShowToast("로그인에 실패했습니다: ${error.message}"))
            }
    }

    private suspend fun assignRandomNickname() {
        val nickname = nicknameRepository.generateRandomNickname()

        nicknameRepository.updateNickname(nickname)
            .onSuccess {
                updateState { it.copy(isLoading = false) }
                navigateNextScreen()
            }
            .onFailure { error ->
                updateState { it.copy(isLoading = false) }
                postSideEffect(LoginSideEffect.ShowToast("로그인에 실패했습니다: ${error.message}"))
                postSideEffect(LoginSideEffect.ShowToast("닉네임 설정에 실패했습니다: ${error.message}"))
            }
    }

    private fun navigateNextScreen() {
        if (uiState.value.isOnboardingExposed) {
            postSideEffect(LoginSideEffect.NavigateHome)
        } else {
            postSideEffect(LoginSideEffect.NavigateOnboarding)
        }
    }
}
