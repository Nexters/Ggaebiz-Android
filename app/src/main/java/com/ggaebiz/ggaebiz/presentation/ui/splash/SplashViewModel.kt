package com.ggaebiz.ggaebiz.presentation.ui.splash

import com.ggaebiz.ggaebiz.domain.repository.AuthRepository
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import kotlinx.coroutines.delay

data class SplashState(
    val isAnimating: Boolean = false,
)

sealed interface SplashSideEffect {
    data object NavigateLogin : SplashSideEffect
    data object NavigateHome : SplashSideEffect
}

sealed interface SplashIntent

class SplashViewModel(
    private val authRepository: AuthRepository,
) : BaseViewModel<SplashState, SplashIntent, SplashSideEffect>(SplashState()) {
    private val initDuration = 1000
    private val animationDuration = 800
    private val delayDuration = 800

    init {
        navigateNextScreen()
    }

    private fun navigateNextScreen() = launch {
        delay(initDuration.toLong())
        updateState { it.copy(isAnimating = true) }
        delay(animationDuration + delayDuration.toLong())

        val isLoggedIn = authRepository.isLoggedIn()
        if (isLoggedIn) {
            postSideEffect(SplashSideEffect.NavigateHome)
        } else {
            postSideEffect(SplashSideEffect.NavigateLogin)
        }
    }
}
