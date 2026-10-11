package com.ggaebiz.ggaebiz.presentation.ui.timer

import com.ggaebiz.ggaebiz.data.model.CharacterName
import com.ggaebiz.ggaebiz.domain.model.TimerRecord
import com.ggaebiz.ggaebiz.domain.repository.ConfigRepository
import com.ggaebiz.ggaebiz.domain.usecase.EndTimerUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetAudioResIdUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetCharacterIdxUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetCurrentTimerUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetIsRestCompletedUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SaveTimerRecordUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SendTimerRecordsUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetSnoozeCountUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import com.ggaebiz.ggaebiz.presentation.common.extension.getCharacterData
import com.ggaebiz.ggaebiz.presentation.service.TimerServiceManager
import com.ggaebiz.ggaebiz.presentation.ui.setting.RestType
import com.ggaebiz.ggaebiz.presentation.ui.setting.TimerMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    val isIntervalTimer: Boolean = false,
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
    private val saveTimerRecordUseCase: SaveTimerRecordUseCase,
    private val sendTimerRecordsUseCase: SendTimerRecordsUseCase,
) : BaseViewModel<TimerState, TimerIntent, TimerSideEffect>(TimerState()) {

    private var timerPlayAt: String = ""
    private var hasRecorded: Boolean = false

    init {
        setTimerSetting()
    }

    fun processIntent(intent: TimerIntent) {
        when (intent) {
            is TimerIntent.StopTimer -> stopTimer()
            is TimerIntent.PauseTimer  -> pauseTimer()
            is TimerIntent.ResumeTimer -> resumeTimer()
        }
    }

    private fun setTimerSetting() = launch {
        val (level, hour, minute, timerMode) = getCurrentTimerUseCase()
        val selectedCharacterIdx = getCharacterIdxUseCase()
        val leveIdx = getCurrentTimerUseCase.getLevelIdx()
        val isIntervalTimer = getCurrentTimerUseCase.getIsIntervalTimer()

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
                remainingSeconds = settingSeconds,
                isIntervalTimer = isIntervalTimer,
            )
        }
        timerPlayAt = currentIsoTime()
        hasRecorded = false

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
        recordTimerSession()
        endTimerUseCase()
    }

    private fun stopTimer() = launch {
        recordTimerSession()
        endTimerUseCase()
        setSnoozeCountUseCase(0)
        postSideEffect(TimerSideEffect.StopService)
    }

    /**
     * 종료된 세션을 로컬 pending 에 append 하고 즉시 전송을 시도한다.
     * 호출 지점: 자연 완료(endTimer) + 수동 종료(stopTimer). 일시정지(pauseTimer)는 호출하지 않는다.
     * time 은 경과초(설정초 − 남은초)라 완료 시엔 설정초, 중간 종료 시엔 실제 경과분이 담긴다.
     * 전송은 방안2 — 성공 응답을 받은 경우에만 로컬에서 제거되고, 실패 시 pending 으로 남아 다음에 재전송된다.
     * 자연 완료 시 flow 가 remainingTime<=0 을 여러 번 방출할 수 있어 hasRecorded 로 1회만 기록한다.
     */
    private suspend fun recordTimerSession() {
        if (hasRecorded) return
        hasRecorded = true

        val state = uiState.value
        val settingSeconds = state.hour * 3600 + state.minute * 60
        val elapsedSeconds = (settingSeconds - state.remainingSeconds).coerceAtLeast(0).toLong()
        val (mode, concentrateType) = state.timerMode.toRecordFields()

        val record = TimerRecord(
            gaebiz = CharacterName.entries.getOrNull(state.selectedCharacterIdx)?.name
                ?: CharacterName.KIKI.name,
            time = elapsedSeconds,
            mode = mode,
            concentrateType = concentrateType,
            playAt = timerPlayAt.ifEmpty { currentIsoTime() },
            restLevel = state.level, // TODO: 서버의 restLevel 정의 확정 후 매핑 조정
        )

        saveTimerRecordUseCase(record)
        launch { sendTimerRecordsUseCase() }
    }

    private fun currentIsoTime(): String =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US).format(Date())

    private fun pauseTimer() {
        updateState {
            it.copy(isPaused = true)
        }
        postSideEffect(TimerSideEffect.PauseService)
    }

    private fun resumeTimer() {
        updateState {
            it.copy(isPaused = false)
        }
        postSideEffect(TimerSideEffect.ResumeService)
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
