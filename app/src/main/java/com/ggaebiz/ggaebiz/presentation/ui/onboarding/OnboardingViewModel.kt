package com.ggaebiz.ggaebiz.presentation.ui.onboarding

import com.ggaebiz.ggaebiz.domain.repository.OnboardingRepository
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel

data class OnboardingState(
    val noneValue: Nothing? = null,
)

sealed interface OnboardingSideEffect {
    data object NavigateHome: OnboardingSideEffect
}

sealed interface OnboardingIntent {
    data object ClickStartGaebizButton : OnboardingIntent
}

class OnboardingViewModel(
    private val onboardingRepository: OnboardingRepository,
) : BaseViewModel<OnboardingState, OnboardingIntent, OnboardingSideEffect>(OnboardingState()) {

    fun processIntent(intent: OnboardingIntent) {
        when (intent) {
            is OnboardingIntent.ClickStartGaebizButton -> clickStartGaebizButton()
        }
    }

    private fun clickStartGaebizButton() = launch {
        onboardingRepository.setIsOnboardingExposed(value = true)
        postSideEffect(OnboardingSideEffect.NavigateHome)
    }
}

