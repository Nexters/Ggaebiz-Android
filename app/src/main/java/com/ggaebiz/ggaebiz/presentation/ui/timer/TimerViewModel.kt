package com.ggaebiz.ggaebiz.presentation.ui.timer

import com.ggaebiz.ggaebiz.domain.repository.ConfigRepository
import com.ggaebiz.ggaebiz.domain.usecase.EndTimerUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetAudioResIdUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetCharacterIdxUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetCurrentTimerUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetIsRestCompletedUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetSnoozeCountUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import com.ggaebiz.ggaebiz.presentation.common.extension.getCharacterData
import com.ggaebiz.ggaebiz.presentation.service.TimerServiceManager
import com.ggaebiz.ggaebiz.presentation.ui.setting.RestType
import com.ggaebiz.ggaebiz.presentation.ui.setting.TimerMode

data class TimerState(
    val selectedCharacterIdx: Int = 0,
    val level: Int = 1,
    val levelIdx: Int = 0,
    val hour: Int = 0,
    val minute: Int = 30,
    val timerMode: TimerMode = TimerMode.Rest(RestType.NORMAL),
    val actionButtonVisible: Boolean = false,
    val remainingSeconds: Int = 0,
    val isPaused: Boolean = false,
)

sealed interface TimerSideEffect {
    data object ShowToast : TimerSideEffect
    data class StartService(
        val seconds: Int,
        val audioResPath: String,
        val vibration : Int,
        val volume : Int,
        val actionButtonVisible: Boolean,
    ) : TimerSideEffect
    data object StopService : TimerSideEffect
    data object PauseService : TimerSideEffect
    data object ResumeService : TimerSideEffect
}

sealed interface TimerIntent {
    data object StopTimer : TimerIntent
    data object PauseTimer : TimerIntent
    data object ResumeTimer : TimerIntent
}

class TimerViewModel(
    private val getAudioResIdUseCase: GetAudioResIdUseCase,
    private val endTimerUseCase: EndTimerUseCase,
    private val getCharacterIdxUseCase: GetCharacterIdxUseCase,
    private val getCurrentTimerUseCase: GetCurrentTimerUseCase,
    private val getIsRestCompletedUseCase: GetIsRestCompletedUseCase,
    private val setSnoozeCountUseCase: SetSnoozeCountUseCase,
    private val configRepository: ConfigRepository,
    private val timerServiceManager: TimerServiceManager,
) : BaseViewModel<TimerState, TimerIntent, TimerSideEffect>(TimerState()) {

    init {
        setTimerSetting()
    }

    fun processIntent(intent: TimerIntent) {
        when (intent) {
            is TimerIntent.StopTimer -> stopTimer()
            is TimerIntent.PauseTimer  -> postSideEffect(TimerSideEffect.PauseService)
            is TimerIntent.ResumeTimer -> postSideEffect(TimerSideEffect.ResumeService)
        }
    }

    private fun setTimerSetting() = launch {
        val (level, hour, minute, timerMode) = getCurrentTimerUseCase()
        val selectedCharacterIdx = getCharacterIdxUseCase()
        val leveIdx = getCurrentTimerUseCase.getLevelIdx()

        val data = selectedCharacterIdx.getCharacterData()
        val audioPath = data?.getMentAudio(timerMode, level - 1, leveIdx)?.audioPath ?: ""
        val settingSeconds = hour * 3600 + minute * 60
        val actionButtonVisible = getIsRestCompletedUseCase().not() && timerMode.isConcentrateTimer()

        val vibration = if(configRepository.getVibrationStatus()){
            configRepository.getVibrationValue()
        }else {
            0
        }

        updateState {
            it.copy(
                selectedCharacterIdx = selectedCharacterIdx,
                level = level,
                levelIdx = leveIdx,
                hour = hour,
                minute = minute,
                timerMode = timerMode,
                actionButtonVisible = actionButtonVisible,
                remainingSeconds = settingSeconds
            )
        }
        postSideEffect(TimerSideEffect.ShowToast)
        postSideEffect(TimerSideEffect.StartService(
            settingSeconds,
            audioPath,
            vibration,
            configRepository.getVolumeValue(),
            actionButtonVisible,
        ))
        bindAndCollectServiceState()
    }

    private fun endTimer() = launch {
        endTimerUseCase()
    }

    private fun stopTimer() = launch {
        endTimerUseCase()
        setSnoozeCountUseCase(0)
        postSideEffect(TimerSideEffect.StopService)
    }

    private fun bindAndCollectServiceState() {
        timerServiceManager.bindTimerService { flow ->
            // flow 수신
            launch {
                flow.collect { info ->
                    updateState {
                        it.copy(
                            remainingSeconds = info.remainingTime,
                            isPaused = info.isPaused
                        )
                    }
                    if (!info.isPaused && info.remainingTime <= 0) {
                        endTimer()
                    }
                }
            }
        }
    }
}
