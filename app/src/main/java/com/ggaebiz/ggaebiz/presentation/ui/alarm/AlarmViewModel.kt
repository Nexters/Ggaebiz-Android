package com.ggaebiz.ggaebiz.presentation.ui.alarm

import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.domain.usecase.GetCharacterIdxUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetIsRestCompletedUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetSnoozeCountUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetCurrentTimerUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetSettingTimerUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetIsRestCompletedUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetSnoozeCountUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetCurrentTimerUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import com.ggaebiz.ggaebiz.presentation.common.extension.getCharacterData
import com.ggaebiz.ggaebiz.presentation.ui.setting.RestType
import com.ggaebiz.ggaebiz.presentation.ui.setting.TimerMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

data class AlarmState(
    val level: Int = 1,
    val ment: Int = R.string.alarm_ment_kiki_level1_1,
    val backGroundImgRes: Int = R.drawable.fullpage_kiki_lev_1,
    val timerMode: TimerMode = TimerMode.Rest(RestType.NORMAL),
    val plusSeconds: String = "+ 00:00",
    var snoozeCount: Int = 0,
    val isRestCompleted: Boolean = false,
) {
    companion object {
        const val DEFAULT_SNOOZE_MINUTE = 5
        const val DEFAULT_REST_MINUTE = 10
    }
    private val isFirstSnoozeOrMaxLevel: Boolean = snoozeCount == 0 && level == 3

    val isRestAvailable: Boolean = timerMode.isConcentrateTimer() && isRestCompleted.not()
    val disableSnoozeButton: Boolean = snoozeCount >= 2 && timerMode.isRestTimer()

    val nextTimerMinute = if (isRestAvailable) DEFAULT_REST_MINUTE else DEFAULT_SNOOZE_MINUTE

    val nextSnoozeCount = if (isRestAvailable) snoozeCount else snoozeCount + 1
    val nextLevel: Int = if (isFirstSnoozeOrMaxLevel || isRestAvailable) level else level + 1
}

sealed interface AlarmIntent {
    data object ClickSnooze : AlarmIntent
    data object ClickResumeConcentrate : AlarmIntent
    data object ClickFinish : AlarmIntent
    data object StartOverCount : AlarmIntent
}

sealed interface AlarmSideEffect {
    data object ClickSnooze : AlarmSideEffect
    data object ClickFinish : AlarmSideEffect
    data object GetOverCount : AlarmSideEffect
}

class AlarmViewModel(
    private val getCharacterIdxUseCase: GetCharacterIdxUseCase,
    private val getCurrentTimerUseCase: GetCurrentTimerUseCase,
    private val setCurrentTimerUseCase: SetCurrentTimerUseCase,
    private val getSettingTimerUseCase: GetSettingTimerUseCase,
    private val getSnoozeCountUseCase: GetSnoozeCountUseCase,
    private val setSnoozeCountUseCase: SetSnoozeCountUseCase,
    private val getIsRestCompletedUseCase: GetIsRestCompletedUseCase,
    private val setIsRestCompletedUseCase: SetIsRestCompletedUseCase,
) : BaseViewModel<AlarmState, AlarmIntent, AlarmSideEffect>(AlarmState()) {

    init {
        getTimerInfo()
        postSideEffect(AlarmSideEffect.GetOverCount)
    }

    fun processIntent(intent: AlarmIntent) {
        when (intent) {
            AlarmIntent.ClickFinish -> finishTimer()
            AlarmIntent.ClickSnooze -> snoozeTimer()
            AlarmIntent.ClickResumeConcentrate -> resumeConcentrateTimer()
            AlarmIntent.StartOverCount -> startIncreaseSeconds()
        }
    }

    fun setTimer(serviceFlow: StateFlow<Int>) = launch {
        val minutes = (serviceFlow.value / 60)
        val seconds = (serviceFlow.value % 60)
        val newPlusSeconds = formatTime(seconds, minutes)
        updateState { it.copy(plusSeconds = newPlusSeconds) }
    }

    private fun startIncreaseSeconds() = launch {
        while (true) {
            delay(1000L) // 1초 대기
            updateState {
                val (minute, second) = parseTime(it.plusSeconds)
                val newSecond = second + 1
                val newPlusSeconds = formatTime(newSecond, minute)
                it.copy(plusSeconds = newPlusSeconds)
            }
        }
    }

    private fun getTimerInfo() = launch {
        val snoozeCount = getSnoozeCountUseCase()
        val characterIdx = getCharacterIdxUseCase()
        val (level, _, _, timerMode) = getCurrentTimerUseCase()
        val levelIdx = getCurrentTimerUseCase.getLevelIdx()
        val isRestCompleted = getIsRestCompletedUseCase()

        val data = characterIdx.getCharacterData()
        if (data != null) {
            updateState {
                it.copy(
                    ment = data.getMentAudio(timerMode, level - 1, levelIdx).ment,
                    backGroundImgRes = data.alarmBackgroundImageList[level - 1],
                    timerMode = timerMode,
                    level = level,
                    snoozeCount = snoozeCount,
                    isRestCompleted = isRestCompleted,
                )
            }
        }
    }

    private fun finishTimer() = launch {
        setSnoozeCountUseCase.invoke(0)
        postSideEffect(AlarmSideEffect.ClickFinish)
    }

    private fun snoozeTimer() = launch {
        setSnoozeCountUseCase(uiState.value.nextSnoozeCount)
        setIsRestCompletedUseCase(true)
        setCurrentTimerUseCase(
            level = uiState.value.nextLevel,
            hour = 0,
            minute = uiState.value.nextTimerMinute,
            timerMode = null,
            isIntervalTimer = true,
        )
        postSideEffect(AlarmSideEffect.ClickSnooze)
    }

    private fun resumeConcentrateTimer() = launch {
        val (hour, minute) = getSettingTimerUseCase()
        setSnoozeCountUseCase(0)
        setIsRestCompletedUseCase(false)
        setCurrentTimerUseCase(
            level = 1,
            hour = hour,
            minute = minute,
            timerMode = uiState.value.timerMode,
            isIntervalTimer = false,
        )
        postSideEffect(AlarmSideEffect.ClickSnooze)
    }

    private fun formatTime(seconds: Int, minute: Int): String {
        val newMinute = if (seconds >= 60) minute + 1 else minute
        val newSecondFormatted = seconds % 60
        return String.format(Locale.KOREA, "+ %02d:%02d", newMinute, newSecondFormatted)
    }

    private fun parseTime(time: String): Pair<Int, Int> {
        val regex = "\\+ (\\d{2}):(\\d{2})".toRegex()
        val matchResult = regex.find(time)
        return if (matchResult != null) {
            val minute = matchResult.groupValues[1].toInt()
            val second = matchResult.groupValues[2].toInt()
            Pair(minute, second)
        } else {
            Pair(0, 0) // 기본값
        }
    }

}
