package com.ggaebiz.ggaebiz.presentation.ui.splash

import com.ggaebiz.ggaebiz.domain.repository.OnboardingRepository
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel

data class SplashState(
    val isOnboardingExposed: Boolean = false,
)

sealed interface SplashSideEffect { }

sealed interface SplashIntent { }

class SplashViewModel(
    private val onboardingRepository: OnboardingRepository,
) : BaseViewModel<SplashState, SplashIntent, SplashSideEffect>(SplashState()) {

    init {
        checkOnboardingExposed()
    }

    private fun checkOnboardingExposed() = launch {
        val status = onboardingRepository.getIsOnboardingExposed()
        updateState { it.copy(isOnboardingExposed = status)}
    }
}

