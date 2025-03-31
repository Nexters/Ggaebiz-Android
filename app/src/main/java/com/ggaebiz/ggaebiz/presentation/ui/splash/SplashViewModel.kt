package com.ggaebiz.ggaebiz.presentation.ui.splash

import com.ggaebiz.ggaebiz.domain.repository.OnboardingRepository
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import kotlinx.coroutines.delay

data class SplashState(
    val isOnboardingExposed: Boolean = false,
    val isAnimating: Boolean = false,
)

sealed interface SplashSideEffect {
    data object NavigateOnboarding: SplashSideEffect
    data object NavigateHome: SplashSideEffect
}

sealed interface SplashIntent { }

class SplashViewModel(
    private val onboardingRepository: OnboardingRepository,
) : BaseViewModel<SplashState, SplashIntent, SplashSideEffect>(SplashState()) {
    private val initDuration = 1000
    private val animationDuration = 800
    private val delayDuration = 800

    init {
        checkOnboardingExposed()
        navigateNextScreen()
    }

    private fun checkOnboardingExposed() = launch {
        val status = onboardingRepository.getIsOnboardingExposed()
        updateState { it.copy(isOnboardingExposed = status)}
    }

    private fun navigateNextScreen() = launch {
        delay(initDuration.toLong())
        updateState { it.copy(isAnimating = true) }
        delay(animationDuration + delayDuration.toLong())
        if (uiState.value.isOnboardingExposed) {
            postSideEffect(SplashSideEffect.NavigateHome)
        } else {
            postSideEffect(SplashSideEffect.NavigateOnboarding)
        }
    }
}

