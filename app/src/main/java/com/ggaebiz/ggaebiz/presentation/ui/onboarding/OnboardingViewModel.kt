package com.ggaebiz.ggaebiz.presentation.ui.onboarding

import com.ggaebiz.ggaebiz.domain.repository.OnboardingRepository
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import kotlinx.coroutines.delay

data class OnboardingState(
    val backPressedOnce: Boolean = false,
)

sealed interface OnboardingSideEffect {
    data object NavigateHome: OnboardingSideEffect
    data object ToastBackPressed: OnboardingSideEffect
}

sealed interface OnboardingIntent {
    data object ClickStartGaebizButton : OnboardingIntent
    data object ClickBackPressedButton : OnboardingIntent
}

class OnboardingViewModel(
    private val onboardingRepository: OnboardingRepository,
) : BaseViewModel<OnboardingState, OnboardingIntent, OnboardingSideEffect>(OnboardingState()) {

    fun processIntent(intent: OnboardingIntent) {
        when (intent) {
            is OnboardingIntent.ClickStartGaebizButton -> clickStartGaebizButton()
            is OnboardingIntent.ClickBackPressedButton -> clickBackPressedButton()
        }
    }

    private fun clickStartGaebizButton() = launch {
        onboardingRepository.setIsOnboardingExposed(value = true)
        postSideEffect(OnboardingSideEffect.NavigateHome)
    }

    private fun clickBackPressedButton() = launch {
        updateState { it.copy(backPressedOnce = true) }
        postSideEffect(OnboardingSideEffect.ToastBackPressed)
        delay(2000)
        updateState { it.copy(backPressedOnce = false) }
    }
}

