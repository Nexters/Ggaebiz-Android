package com.ggaebiz.ggaebiz.presentation.ui.timer

import com.ggaebiz.ggaebiz.domain.usecase.EndTimerUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetAudioResIdUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetCharacterIdxUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetTimerSettingUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetSnoozeCountUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import com.ggaebiz.ggaebiz.presentation.common.extension.getCharacterData
import kotlinx.coroutines.delay

data class TimerState(
    val selectedCharacterIdx: Int = 0,
    val level: Int = 1,
    val levelIdx: Int = 0,
    val hour: Int = 0,
    val minute: Int = 30,
    val remainingSeconds: Int = 0,
)

sealed interface TimerSideEffect {
    data object ShowToast : TimerSideEffect
    data class StartService(val seconds: Int, val audioResPath: String) : TimerSideEffect
    data object StopService : TimerSideEffect
}

sealed interface TimerIntent {
    data object StopTimer : TimerIntent
}

class TimerViewModel(
    private val getAudioResIdUseCase: GetAudioResIdUseCase,
    private val endTimerUseCase: EndTimerUseCase,
    private val getCharacterIdxUseCase: GetCharacterIdxUseCase,
    private val getTimerSettingUseCase: GetTimerSettingUseCase,
    private val setSnoozeCountUseCase: SetSnoozeCountUseCase,
) : BaseViewModel<TimerState, TimerIntent, TimerSideEffect>(TimerState()) {

    init {
        setTimerSetting()
    }

    fun processIntent(intent: TimerIntent) {
        when (intent) {
            is TimerIntent.StopTimer -> stopTimer()
        }
    }

    private fun setTimerSetting() = launch {
        val (level, hour, minute) = getTimerSettingUseCase()
        val selectedCharacterIdx = getCharacterIdxUseCase()
        val leveIdx = getTimerSettingUseCase.getLevelIdx()

        val data = selectedCharacterIdx.getCharacterData()
        val audioPath = (data?.mentAudioList?.get(level - 1)?.get(leveIdx)?.audioPath) ?: ""
        val settingSeconds = hour * 3600 + minute * 60

        updateState {
            it.copy(
                selectedCharacterIdx = selectedCharacterIdx,
                level = level,
                levelIdx = leveIdx,
                hour = hour,
                minute = minute,
                remainingSeconds = settingSeconds
            )
        }
        postSideEffect(TimerSideEffect.ShowToast)
        postSideEffect(TimerSideEffect.StartService(settingSeconds, audioPath))
        startTimeTick()
    }

    private fun startTimeTick() = launch {
        while (uiState.value.remainingSeconds > 0) {
            delay(1000L)
            updateState { it.copy(remainingSeconds = uiState.value.remainingSeconds - 1) }
        }
        endTimer()
    }

    private fun endTimer() = launch {
        endTimerUseCase()
    }

    private fun stopTimer() = launch {
        endTimerUseCase()
        setSnoozeCountUseCase(0)
        postSideEffect(TimerSideEffect.StopService)
    }
}
