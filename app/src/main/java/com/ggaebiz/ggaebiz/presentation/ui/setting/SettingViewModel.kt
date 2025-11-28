package com.ggaebiz.ggaebiz.presentation.ui.setting

import com.ggaebiz.ggaebiz.domain.repository.OnboardingRepository
import com.ggaebiz.ggaebiz.domain.usecase.GetCharacterIdxUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetCurrentTimerUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetIsRestCompletedUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetSettingTimerUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import kotlinx.coroutines.delay

data class SettingState(
    val selectedCharacterIdx: Int = 0,
    val level: Int = 1,
    val isNudgeGuideViewed: Boolean = true,
    val selectedHour: String = TimerMode.Rest(RestType.NORMAL).defaultHour,
    val selectedMinute: String = TimerMode.Rest(RestType.NORMAL).defaultMinute,
    val maxHour: Int = TimerMode.Rest(RestType.NORMAL).maxHour,
    val maxMinute: Int = TimerMode.Rest(RestType.NORMAL).maxMinute,
    val timerMode: TimerMode = TimerMode.Rest(RestType.NORMAL),
    val isLevelPopupVisible: Boolean = false,
)

sealed interface SettingSideEffect {
    data object NavigateToTimer : SettingSideEffect
}

sealed interface SettingIntent {
    data class SelectLevel(val level: Int) : SettingIntent
    data class ClickStartButton(val hour: Int, val minute: Int) : SettingIntent
    data class SelectHour(val hour: String) : SettingIntent
    data class SelectMinute(val minute: String) : SettingIntent
    data class ClickTimerMode(val timerMode: TimerMode) : SettingIntent
    data class ClickMentLevel(val clickMentLevel: Boolean) : SettingIntent
    data object EnterScreen : SettingIntent
    data object CloseNudgePopUp : SettingIntent
}

class SettingViewModel(
    private val getCharacterIdxUseCase: GetCharacterIdxUseCase,
    private val setCurrentTimerUseCase: SetCurrentTimerUseCase,
    private val setSettingTimerUseCase: SetSettingTimerUseCase,
    private val setIsRestCompletedUseCase: SetIsRestCompletedUseCase,
    private val onboardingRepository: OnboardingRepository
) : BaseViewModel<SettingState, SettingIntent, SettingSideEffect>(SettingState()) {

    init {
        getCharacterIdx()
    }

    private fun getCharacterIdx() = launch {
        val selectedCharacterIdx = getCharacterIdxUseCase()
        updateState { it.copy(selectedCharacterIdx = selectedCharacterIdx) }
    }

    fun processIntent(intent: SettingIntent) = launch {
        when (intent) {
            is SettingIntent.SelectLevel -> selectLevel(level = intent.level)
            is SettingIntent.ClickStartButton -> startTimer(intent.hour, intent.minute)
            is SettingIntent.ClickTimerMode -> switchTimerMode(intent.timerMode)
            is SettingIntent.SelectHour -> selectHour(intent.hour)
            is SettingIntent.SelectMinute -> selectMinute(intent.minute)
            is SettingIntent.ClickMentLevel -> clickMentLevel(intent.clickMentLevel)
            SettingIntent.EnterScreen -> launch {
                if (onboardingRepository.getSettingNudgeGuideViewed()) {
                    updateState { it.copy(isNudgeGuideViewed = true) }
                } else {
                    delay(200)
                    updateState { it.copy(isNudgeGuideViewed = false) }
                }
            }
            SettingIntent.CloseNudgePopUp -> launch {
                updateState { it.copy(isNudgeGuideViewed = true) }
                onboardingRepository.setSettingNudgeGuideViewed(true)
            }
        }
    }

    private fun selectLevel(level: Int) = launch {
        updateState { it.copy(level = level) }
    }

    private fun selectHour(hour: String) = launch {
        updateState { it.copy(selectedHour = hour) }
    }

    private fun selectMinute(minute: String) = launch {
        updateState { it.copy(selectedMinute = minute) }
    }

    private fun clickMentLevel(isLevelPopupVisible: Boolean) = launch {
        updateState { it.copy(isLevelPopupVisible = isLevelPopupVisible) }
    }

    private fun startTimer(hour: Int, minute: Int) = launch {
        setCurrentTimerUseCase(
            level = if (uiState.value.timerMode.isConcentrateTimer()) 1 else uiState.value.level,
            hour = hour,
            minute = minute,
            timerMode = uiState.value.timerMode,
        )
        setSettingTimerUseCase(
            hour = hour,
            minute = minute,
        )
        setIsRestCompletedUseCase(false)
        postSideEffect(SettingSideEffect.NavigateToTimer)
    }

    private fun switchTimerMode(timerMode: TimerMode) = launch {
        updateState {
            it.copy(
                level = 1,
                timerMode = timerMode,
                maxHour = timerMode.maxHour,
                maxMinute = timerMode.maxMinute,
                selectedHour = timerMode.defaultHour,
                selectedMinute = timerMode.defaultMinute,
            )
        }
    }
}
