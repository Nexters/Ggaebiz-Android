package com.ggaebiz.ggaebiz.presentation.ui.onboarding

import com.ggaebiz.ggaebiz.domain.repository.OnboardingRepository
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import com.ggaebiz.ggaebiz.presentation.ui.onboarding.Onboarding.Companion.ONBOARDING_LIST
import kotlinx.coroutines.delay

data class OnboardingState(
    val backPressedOnce: Boolean = false,
    val currentPage: Int = 0,
)

sealed interface OnboardingSideEffect {
    data object NavigateHome: OnboardingSideEffect
    data object ToastBackPressed: OnboardingSideEffect
}

sealed interface OnboardingIntent {
    data class SwipePager(val page: Int) : OnboardingIntent
    data object ClickNextButton : OnboardingIntent
    data object ClickSkipButton : OnboardingIntent
    data object ClickBackButton : OnboardingIntent
    data object ClickStartGaebizButton : OnboardingIntent
    data object ClickBackPressedButton : OnboardingIntent
}

class OnboardingViewModel(
    private val onboardingRepository: OnboardingRepository,
) : BaseViewModel<OnboardingState, OnboardingIntent, OnboardingSideEffect>(OnboardingState()) {

    fun processIntent(intent: OnboardingIntent) {
        when (intent) {
            is OnboardingIntent.SwipePager -> swipePager(intent.page)
            is OnboardingIntent.ClickNextButton -> clickNextButton()
            is OnboardingIntent.ClickSkipButton -> clickSkipButton()
            is OnboardingIntent.ClickBackButton -> clickBackButton()
            is OnboardingIntent.ClickStartGaebizButton -> clickStartGaebizButton()
            is OnboardingIntent.ClickBackPressedButton -> clickBackPressedButton()
        }
    }

    private fun swipePager(page: Int) = launch {
        updateState { it.copy(currentPage = page) }
    }

    private fun clickNextButton() = launch {
        updateState { it.copy(currentPage = it.currentPage.inc()) }
    }

    private fun clickSkipButton() = launch {
        updateState { it.copy(currentPage = ONBOARDING_LIST.size - 1) }
    }

    private fun clickBackButton() = launch {
        updateState { it.copy(currentPage = it.currentPage.dec()) }
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

