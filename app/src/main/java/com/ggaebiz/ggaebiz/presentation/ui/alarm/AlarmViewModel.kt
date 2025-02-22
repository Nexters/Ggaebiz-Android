package com.ggaebiz.ggaebiz.presentation.ui.alarm

import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.domain.usecase.GetCharacterIdxUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetSnoozeCountUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetTimerSettingUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetSnoozeCountUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetTimerSettingUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import com.ggaebiz.ggaebiz.presentation.common.extension.getCharacterData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

data class AlarmState(
    val level: Int = 1,
    val ment: Int = R.string.alarm_ment_kiki_level1_1,
    val backGroundImgRes: Int = R.drawable.fullpage_kiki_lev_1,
    val plusSeconds: String = "+ 00:00",
    var snoozeCount: Int = 0,
) {
    val disableSnoozeButton: Boolean = snoozeCount >= 2
}

sealed interface AlarmIntent {
    data object ClickSnooze : AlarmIntent
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
    private val getTimerSettingUseCase: GetTimerSettingUseCase,
    private val setTimerSettingUseCase: SetTimerSettingUseCase,
    private val getSnoozeCountUseCase: GetSnoozeCountUseCase,
    private val setSnoozeCountUseCase: SetSnoozeCountUseCase,
) : BaseViewModel<AlarmState, AlarmIntent, AlarmSideEffect>(AlarmState()) {

    init {
        getTimerInfo()
        postSideEffect(AlarmSideEffect.GetOverCount)
    }

    fun processIntent(intent: AlarmIntent) {
        when (intent) {
            AlarmIntent.ClickFinish -> finishTimer()
            AlarmIntent.ClickSnooze -> snoozeTimer()
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
        val (level, _, _) = getTimerSettingUseCase()
        val levelIdx = getTimerSettingUseCase.getLevelIdx()

        val data = characterIdx.getCharacterData()
        if (data != null) {
            updateState {
                it.copy(
                    ment = data.mentAudioList[level - 1][levelIdx].ment,
                    backGroundImgRes = data.alarmBackgroundImageList[level - 1],
                    level = level,
                    snoozeCount = snoozeCount
                )
            }
        }
    }

    private fun finishTimer() = launch {
        setSnoozeCountUseCase.invoke(0)
        postSideEffect(AlarmSideEffect.ClickFinish)
    }

    private fun snoozeTimer() = launch {
        val nowLevel = uiState.value.level
        val nowSnooze = uiState.value.snoozeCount
        val newLevel = if (nowSnooze == 0 && nowLevel == 3) nowLevel else nowLevel + 1

        setSnoozeCountUseCase.invoke((nowSnooze + 1))
        setTimerSettingUseCase(
            level = newLevel,
            hour = 0,
            minute = 5,
            snoozeCount = nowSnooze + 1
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
