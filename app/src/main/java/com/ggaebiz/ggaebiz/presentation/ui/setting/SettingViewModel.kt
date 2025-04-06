package com.ggaebiz.ggaebiz.presentation.ui.setting

import com.ggaebiz.ggaebiz.domain.repository.ConfigRepository
import com.ggaebiz.ggaebiz.domain.usecase.GetCharacterIdxUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetTimerSettingUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import kotlinx.coroutines.delay

data class SettingState(
    val selectedCharacterIdx: Int = 0,
    val level: Int = 1,
    val isNudgeGuideViewed: Boolean = true,
)

sealed interface SettingSideEffect {
    data object NavigateToTimer : SettingSideEffect
}

sealed interface SettingIntent {
    data class SelectLevel(val level: Int) : SettingIntent
    data class ClickStartButton(val hour: Int, val minute: Int) : SettingIntent
    data object EnterScreen : SettingIntent
    data object CloseNudgePopUp : SettingIntent
}

class SettingViewModel(
    private val getCharacterIdxUseCase: GetCharacterIdxUseCase,
    private val setTimerSettingUseCase: SetTimerSettingUseCase,
    private val configRepository: ConfigRepository,
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
            SettingIntent.EnterScreen -> launch{
                if (configRepository.getSettingNudgeGuideViewed()) {
                    updateState { it.copy(isNudgeGuideViewed = true) }
                }else{
                    delay(200)
                    updateState { it.copy(isNudgeGuideViewed = false) }
                }
            }
            SettingIntent.CloseNudgePopUp -> launch {
                updateState { it.copy(isNudgeGuideViewed = true) }
                configRepository.setSettingNudgeGuideViewed(true)
            }
        }
    }

    private fun selectLevel(level: Int) = launch {
        updateState { it.copy(level = level) }
    }

    private fun startTimer(hour: Int, minute: Int) = launch {
        setTimerSettingUseCase(
            level = uiState.value.level, hour = hour, minute = minute, snoozeCount = 0
        )
        postSideEffect(SettingSideEffect.NavigateToTimer)
    }
}
