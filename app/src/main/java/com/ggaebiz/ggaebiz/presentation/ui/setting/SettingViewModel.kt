package com.ggaebiz.ggaebiz.presentation.ui.setting

import com.ggaebiz.ggaebiz.domain.usecase.GetCharacterIdxUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetTimerSettingUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel

data class SettingState(
    val selectedCharacterIdx: Int = 0,
    val level: Int = 1,
)

sealed interface SettingSideEffect {
    data object NavigateToTimer: SettingSideEffect
}

sealed interface SettingIntent {
    data class SelectLevel(val level: Int): SettingIntent
    data class ClickStartButton(val hour: Int, val minute: Int): SettingIntent
}

class SettingViewModel(
    private val getCharacterIdxUseCase: GetCharacterIdxUseCase,
    private val setTimerSettingUseCase: SetTimerSettingUseCase
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
        }
    }

    private fun selectLevel(level: Int) = launch {
        updateState { it.copy(level = level) }
    }

    private fun startTimer(hour: Int, minute: Int) = launch {
        setTimerSettingUseCase(
            level = uiState.value.level,
            hour = hour,
            minute = minute,
            snoozeCount = 0
        )
        postSideEffect(SettingSideEffect.NavigateToTimer)
    }
}
