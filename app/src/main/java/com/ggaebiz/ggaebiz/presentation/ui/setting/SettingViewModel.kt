package com.ggaebiz.ggaebiz.presentation.ui.setting

import com.ggaebiz.ggaebiz.domain.usecase.GetCharacterIdxUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetTimerSettingUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel

data class SettingState(
    val selectedCharacterIdx: Int = 0,
    val level: Int = 1,
    val hour: Int = 0,
    val minute: Int = 30,
    val buttonEnabled: Boolean = true
)

sealed interface SettingSideEffect {
    data object NavigateToTimer: SettingSideEffect
}

sealed interface SettingIntent {
    data class SelectLevel(val level: Int): SettingIntent
    data class SelectTime(val hour: Int, val minute: Int): SettingIntent
    data object ClickStartButton: SettingIntent
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
            is SettingIntent.SelectTime -> selectTime(hour = intent.hour, minute = intent.minute)
            is SettingIntent.ClickStartButton -> startTimer()
        }
    }

    private fun selectLevel(level: Int) = launch {
        updateState { it.copy(level = level) }
    }

    private fun selectTime(hour: Int, minute: Int) = launch {
        updateState {
            it.copy(
                hour = hour,
                minute = minute,
                buttonEnabled = hour != 0 || minute != 0,
            )
        }
    }

    private fun startTimer() = launch {
        setTimerSettingUseCase(
            level = uiState.value.level,
            hour = uiState.value.hour,
            minute = uiState.value.minute,
        )
        postSideEffect(SettingSideEffect.NavigateToTimer)
    }
}
